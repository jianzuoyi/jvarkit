/*
The MIT License (MIT)

Copyright (c) 2014 Pierre Lindenbaum

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


History:
* 2014 creation

*/
package com.github.lindenb.jvarkit.tools.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.GenotypesContext;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFFormatHeaderLine;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFHeaderLineType;
import htsjdk.samtools.util.CloseableIterator;
import htsjdk.samtools.util.CloserUtil;

import com.beust.jcommander.Parameter;
import com.github.lindenb.jvarkit.io.IOUtils;
import com.github.lindenb.jvarkit.util.jcommander.Launcher;
import com.github.lindenb.jvarkit.util.jcommander.Program;
import com.github.lindenb.jvarkit.util.log.Logger;
import com.github.lindenb.jvarkit.util.vcf.TabixVcfFileReader;
import com.github.lindenb.jvarkit.util.vcf.VCFUtils;
import com.github.lindenb.jvarkit.util.vcf.VcfIterator;
/** 
 BEGIN_DOC
 
## Example

```
$ find ./ -name "*.vcf" -o -name "*.vcf.gz" |\
   java -jar dist/findamutation.jar -p "chr1:1234" 


htsjdk/testdata/htsjdk/samtools/intervallist/IntervalListFromVCFTestManual.vcf	1	8216713	8216713	yossi-1		NA12878	HET	A G
htsjdk/testdata/htsjdk/samtools/intervallist/IntervalListFromVCFTestManual.vcf	1	8216713	8216713	yossi-1		NA12891	HET	A G
htsjdk/testdata/htsjdk/samtools/intervallist/IntervalListFromVCFTestManual.vcf	1	8216713	8216713	yossi-1		NA12892	HET	A G
htsjdk/testdata/htsjdk/samtools/intervallist/IntervalListFromVCFTestManual.vcf	2	2	2	.		NA12878	HOM_REF	C C
htsjdk/testdata/htsjdk/samtools/intervallist/IntervalListFromVCFTestManual.vcf	2	2	2	.		NA12891	HET	C T
htsjdk/testdata/htsjdk/samtools/intervallist/IntervalListFromVCFTestManual.vcf	2	2	2	.		NA12892	HET	C T
```

 
 END_DOC
 */
@Program(name="findavariation",
	description="Finds a specific mutation in a list of VCF files",
	keywords={"vcf","variation","search"})
public class FindAVariation extends Launcher
	{
	private static final Logger LOG = Logger.build(FindAVariation.class).make();
	@Parameter(names={"-p","--position"},description="A list of 'chrom/position'")
	private Set<String> positionsList = new HashSet<>();
	@Parameter(names={"-o","--out"},description=OPT_OUPUT_FILE_OR_STDOUT)
	private File outputFile = null;
	@Parameter(names={"-f","--posfile"},description="Add this file containing chrom:position")
	private Set<String> positionFilesList = new HashSet<>();
	@Parameter(names={"-homref","--homref"},description="Hide HOM_REF genotypes")
	private boolean hideHomRef=false;
	@Parameter(names={"-nocall","--nocall"},description="Hide NO_CALL genotypes")
	private boolean hideNoCall=false;
	@Parameter(names={"-snp","--snp"},description="Search only variant have the very same position (ignore overlapping variants)")
	private boolean onlySnp=false;
	
	private static class Mutation
		{
		final String chrom;
		final int pos;
		Mutation(final String chrom,final int pos)
			{
			this.chrom=chrom;
			this.pos=pos;
			}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + chrom.hashCode();
			result = prime * result + pos;
			return result;
			}
		@Override
		public boolean equals(final Object obj) {
			if (this == obj)return true;
			final Mutation other = (Mutation) obj;
			if (pos != other.pos) return false;
			 if (!chrom.equals(other.chrom))
				return false;
			
			return true;
		}
		
		@Override
		public String toString() {
			return chrom+":"+pos;
			}
		
		}
	private final Set<Mutation> mutations=new HashSet<Mutation>();
	private PrintWriter out=null;
	
    private FindAVariation()
    	{
    	}		
   
    
    private void reportPos(final File f,final VCFHeader header,final VariantContext ctx)
		{
		out.print(f);
		out.print('\t');
		out.print(ctx.getContig());
		out.print('\t');
		out.print(ctx.getStart());
		out.print('\t');
		out.print(ctx.getEnd());
		out.print('\t');
		out.print(ctx.hasID()?ctx.getID():".");
		out.print('\t');
		out.print(ctx.getReference().getDisplayString());
		}	

    
    private void report(
    		final File f,
    		final VCFHeader header,
    		final VariantContext ctx,
    		final Mutation mpos
    		)
    	{
    	
    	final GenotypesContext genotypes=ctx.getGenotypes();
    	if(genotypes==null || genotypes.isEmpty())
    		{
    		reportPos(f,header,ctx);
    		out.println();
    		}
    	else
    		{
    		VCFFormatHeaderLine DP4header = header.getFormatHeaderLine("DP4");
    		if(DP4header!=null &&
    			!( DP4header.getType().equals(VCFHeaderLineType.Integer) &&
    				DP4header.getCount()==4))
    			{
    			DP4header=null;
    			}
    		for(int i=0;i< genotypes.size();++i)
    			{
    			Genotype g=genotypes.get(i);
    			if(!g.isCalled() && this.hideNoCall) continue;
    			if(g.isHomRef() && this.hideHomRef) continue;
    			reportPos(f,header,ctx);
    			out.print('\t');
    			out.print(g.getSampleName());
    			out.print('\t');
    			out.print(g.getType());
    			out.print('\t');
    			List<Allele> alleles=g.getAlleles();
    			for(int na=0;na<alleles.size();++na)
    				{
    				if(na>0) out.print(" ");
    				out.print(alleles.get(na).getDisplayString());
    				}
    			if(DP4header!=null)
    				{
    				final  Object dp4=g.getExtendedAttribute("DP4");
    				if(dp4!=null )
    					{
    					out.print('\t');
    					out.print(String.valueOf(dp4));//it's a String not an int[] ??
    					}
    				}
    			out.println();
    			}
    		}
    	}	
    
    private Set<Mutation> convertFromVcfHeader(final File f,final VCFHeader h)
    	{
    	final Set<Mutation> copy=new HashSet<Mutation>(this.mutations.size());
    	for(final Mutation m:this.mutations)
    		{
    		final String s=VCFUtils.findChromNameEquivalent(m.chrom,h);
    		if(s==null)
    			{
    			LOG.warn("Cannot convert chrom "+s+" in "+f);
    			continue;
    			}
    		copy.add(new Mutation(s, m.pos));
    		}
    	return copy;
    	}

    private void scan(final BufferedReader in) throws IOException
    	{
    	String line;
    	while((line=in.readLine())!=null)
			{
			if(line.isEmpty() || line.startsWith("#")) continue;
			final File f=new File(line);
			if(!f.isFile()) continue;
			if(!f.canRead()) continue;
			if(!VCFUtils.isVcfFile(f)) continue;
			VcfIterator iter=null;
			
			
			if(VCFUtils.isTribbleVcfFile(f) )
				{
				VCFFileReader r=null;
    			try
					{
					r=new VCFFileReader(f,true);
					final VCFHeader header =r.getFileHeader();
					for(final Mutation m:convertFromVcfHeader(f,header))
						{
						final CloseableIterator<VariantContext> iter2 = r.query(
								m.chrom, m.pos, m.pos);
						while(iter2.hasNext())
							{
							final VariantContext ctx=iter2.next();
							if(this.onlySnp )
								{	
								if(ctx.getStart()!=m.pos || ctx.getEnd()!=m.pos) continue;
								}
							report(f,header,ctx,m);
							}
						CloserUtil.close(iter2);
						}
					}
    			catch(final htsjdk.tribble.TribbleException.InvalidHeader err)
    				{
    				LOG.warn(f+"\t"+err.getMessage());
    				}
				catch(final Exception err)
					{
					LOG.severe("cannot read "+f,err);
					}
				finally
					{
					CloserUtil.close(r);
					}    				
				}
			else if(VCFUtils.isTabixVcfFile(f)) {
				TabixVcfFileReader r=null;
    			try
					{
					r=new TabixVcfFileReader(f.getPath());
					final VCFHeader header =r.getHeader();
					for(final Mutation m:convertFromVcfHeader(f,header))
						{
						final Iterator<VariantContext> iter2 = r.iterator(
								m.chrom, m.pos, m.pos);
						while(iter2.hasNext())
							{
							final VariantContext ctx=iter2.next();
							if(this.onlySnp )
								{	
								if(ctx.getStart()!=m.pos || ctx.getEnd()!=m.pos) continue;
								}
							report(f,header,ctx,m);
							}
						CloserUtil.close(iter2);
						}
					}
    			catch(final htsjdk.tribble.TribbleException.InvalidHeader err)
    				{
    				LOG.warn(f+"\t"+err.getMessage());
    				}
				catch(final Exception err)
					{
					LOG.severe("cannot read "+f,err);
					}
				finally
					{
					CloserUtil.close(r);
					}    				
				}
			else
				{
				try
					{
					iter=VCFUtils.createVcfIteratorFromFile(f);
					final VCFHeader header = iter.getHeader();
					final Set<Mutation> mutlist=convertFromVcfHeader(f,iter.getHeader());
					while(iter.hasNext())
						{
						final VariantContext ctx=iter.next();
						final Mutation m=new Mutation(ctx.getContig(), ctx.getStart());
						
						for(final Mutation m2: mutlist)
							{
							if(m.equals(m2)) {
						    	if(this.onlySnp )
									{	
									if(ctx.getStart()!=m2.pos || ctx.getEnd()!=m2.pos) continue;
									}	
								report(f,header,ctx,m2);
								break;
								}
							}
						}
					}
				catch(final htsjdk.tribble.TribbleException.InvalidHeader err)
    				{
    				LOG.warn(f+"\t"+err.getMessage());
    				}
				catch(final Exception err)
					{
					LOG.severe("Error in "+f,err);
					}
				finally
					{
					CloserUtil.close(iter);
					}
				}
    			
			}
    	}
    

	private Mutation parseMutation(final String s)
		{
		final int colon=s.indexOf(':');
		if(colon==-1 || colon+1==s.length())
			{
			throw new IllegalArgumentException("Bad chrom:pos "+s);
			}
		
		final String chrom=s.substring(0,colon).trim();
		if(chrom.isEmpty())
			{
			throw new IllegalArgumentException("Bad chrom:pos "+s);
			}
		final Mutation m=new Mutation(chrom, Integer.parseInt(s.substring(colon+1)));
		return m;
		}
	
	@Override
	public int doWork(final List<String> args) {
		BufferedReader r=null;
		try
			{
			
			for(final String f:this.positionFilesList)
				{
				r = IOUtils.openURIForBufferedReading(f);
				String line;
				while((line=r.readLine())!=null)
					{
					if(line.isEmpty() || line.startsWith("#")) continue;
					final Mutation m= parseMutation(line);
					LOG.debug("adding "+m);
					this.mutations.add(m);
					}
				}
			
			for(final String s:this.positionsList)
				{
				final Mutation m= parseMutation(s);
				LOG.debug("adding "+m);
				this.mutations.add(m);
				}			
			
			this.out=super.openFileOrStdoutAsPrintWriter(this.outputFile);
			this.out.println("#FILE\tCHROM\tstart\tend\tID\tREF\tsample\ttype\tALLELES\tDP4");
			if(args.isEmpty())
				{
				LOG.info("Reading from stdin");
				scan(new BufferedReader(new InputStreamReader(stdin())));
				}
			else
				{
				for(final String filename: args)
					{
					LOG.info("Reading from "+filename);
					r=IOUtils.openURIForBufferedReading(filename);
					scan(r);
					r.close();
					}
				}
			this.out.flush();
			this.out.close();
			this.out=null;
			return 0;
			}
		catch(final Exception err)
			{
			LOG.error(err);
			return -1;
			}
		finally
			{
			CloserUtil.close(out);
			CloserUtil.close(r);
			}
		}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new FindAVariation().instanceMainWithExit(args);

	}

}
