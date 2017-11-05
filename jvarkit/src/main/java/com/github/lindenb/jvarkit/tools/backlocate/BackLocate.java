/*
The MIT License (MIT)

Copyright (c) 2017 Pierre Lindenbaum

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


*/
package com.github.lindenb.jvarkit.tools.backlocate;

import htsjdk.samtools.reference.IndexedFastaSequenceFile;
import htsjdk.samtools.util.CloserUtil;
import htsjdk.samtools.util.Interval;
import htsjdk.tribble.annotation.Strand;
import htsjdk.tribble.readers.LineIterator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.beust.jcommander.Parameter;
import com.github.lindenb.jvarkit.io.IOUtils;
import com.github.lindenb.jvarkit.lang.AbstractCharSequence;
import com.github.lindenb.jvarkit.lang.JvarkitException;
import com.github.lindenb.jvarkit.util.bio.GeneticCode;
import com.github.lindenb.jvarkit.util.jcommander.Launcher;
import com.github.lindenb.jvarkit.util.jcommander.Program;
import com.github.lindenb.jvarkit.util.log.Logger;
import com.github.lindenb.jvarkit.util.picard.GenomicSequence;
import com.github.lindenb.jvarkit.util.ucsc.KnownGene;
/**
 BEGIN_DOC

## Example

mutation P->M at 1090 in NOTCH2

```
$  echo -e "NOTCH2\tP1090M" | java -jar dist/backlocate.jar -R hg19.fa
(...)
[WARNING/BackLocate] 2014-11-05 12:03:08 "The reference doesn't contain chromosome chr17_ctg5_hap1"
[WARNING/BackLocate] 2014-11-05 12:03:15 "The reference doesn't contain chromosome chr4_ctg9_hap1"
[WARNING/BackLocate] 2014-11-05 12:03:16 "The reference doesn't contain chromosome chr6_apd_hap1"
[WARNING/BackLocate] 2014-11-05 12:03:16 "The reference doesn't contain chromosome chr6_cox_hap2"
[WARNING/BackLocate] 2014-11-05 12:03:16 "The reference doesn't contain chromosome chr6_dbb_hap3"
(...)
[INFO/BackLocate] 2014-11-05 12:03:18 "genes:78963"
[INFO/BackLocate] 2014-11-05 12:03:18 "loading http://hgdownload.cse.ucsc.edu/goldenPath/hg19/database/kgXref.txt.gz"
[INFO/BackLocate] 2014-11-05 12:03:24 "kgxref:28493"
(...)
```

```
#User.Gene	AA1	petide.pos.1	AA2	knownGene.name	knownGene.strandknownGene.AA	index0.in.rna	codon	base.in.rna	chromosome	index0.in.genomic	exon
##uc001eik.3
NOTCH2	P	1090	M	uc001eik.3	NEGATIVE	P	3267	CCA	C	chr1	120480548	Exon 20
NOTCH2	P	1090	M	uc001eik.3	NEGATIVE	P	3268	CCA	C	chr1	120480547	Exon 20
NOTCH2	P	1090	M	uc001eik.3	NEGATIVE	P	3269	CCA	A	chr1	120480546	Exon 20
##uc001eil.3
NOTCH2	P	1090	M	uc001eil.3	NEGATIVE	P	3267	CCA	C	chr1	120480548	Exon 20
NOTCH2	P	1090	M	uc001eil.3	NEGATIVE	P	3268	CCA	C	chr1	120480547	Exon 20
NOTCH2	P	1090	M	uc001eil.3	NEGATIVE	P	3269	CCA	A	chr1	120480546	Exon 20
```


## See also

 * http://plindenbaum.blogspot.fr/2011/03/mapping-mutation-on-protein-to-genome.html
 * https://github.com/lindenb/jvarkit/issues/14
 * https://github.com/lindenb/jvarkit/issues/13


## History

 * 2017: Moved to jcommander
 * 2014: Moved to jvarkit
 * Nov 2014 : removed all the dependencies to SQL and DAS; use a local indexed genome
 * Aug 2015 : Added a new column "potention var codon" (as https://twitter.com/_ramrs/status/631123002005061633 ) , renamed "codon" to "wild codon"

## Cited in

backlocate was cited in:

 * CRISPR-STOP: gene silencing through base-editing-induced nonsense mutations. 2017 Nat Meth. [http://dx.doi.org/10.1038/nmeth.4327](http://dx.doi.org/10.1038/nmeth.4327).

 
 END_DOC
 */
@Program(name="backlocate",
	description="Mapping a mutation on a protein back to the genome.",
	keywords={"vcf","annotation","prediction","protein"},
	biostars=116366
	)
public class BackLocate
	extends Launcher
	{
	private static final Logger LOG = Logger.build(BackLocate.class).make();
	@Parameter(names={"-p","--printSeq"},description="print mRNA & protein sequences")
	private boolean printSequences = false;

	@Parameter(names={"-k","--kg"},description=KnownGene.OPT_KNOWNGENE_DESC)
	private String knownGeneURI = KnownGene.getDefaultUri();

	@Parameter(names={"-x","--kgxref"},description="UCSC kgXRef URI")
	private String kgXRef = "http://hgdownload.cse.ucsc.edu/goldenPath/hg19/database/kgXref.txt.gz";

	@Parameter(names={"-R","--reference"},description=INDEXED_FASTA_REFERENCE_DESCRIPTION,required=true)
	private File indexedRefFile=null;
	
	@Parameter(names={"-o","--out"},description=OPT_OUPUT_FILE_OR_STDOUT)
	private File outputFile=null;

	

	private IndexedFastaSequenceFile indexedFastaSequenceFile=null;
	private GenomicSequence genomicSeq=null;
	private final Map<String,Set<String>> geneSymbol2kg=new HashMap<>();
	private final Map<String,KnownGene> knwonGenes=new HashMap<>();
	/** get a genetic code from a chromosome name (either std or mitochondrial */
	private static GeneticCode getGeneticCodeByChromosome(final String chr)
		{
		if(chr.equalsIgnoreCase("chrM") || chr.equalsIgnoreCase("MT")) return GeneticCode.getMitochondrial();
		return GeneticCode.getStandard();
		}
		
	
	static private class RNASequence extends AbstractCharSequence
		{
		final List<Integer> genomicPositions=new ArrayList<Integer>();
		final GenomicSequence genomic;
		final char strand;
		RNASequence(final GenomicSequence genomic,final char strand)
			{
			this.genomic=genomic;
			this.strand=strand;
			}
		@Override
		public char charAt(int i)
			{
			char c=genomic.charAt(this.genomicPositions.get(i));
			return (strand=='+'?c:complement(c));
			}
		@Override
		public int length()
			{
			return genomicPositions.size();
			}
		}
	
	static private class ProteinCharSequence extends AbstractCharSequence
		{
		private final RNASequence cDNA;
		private final GeneticCode geneticCode;
		ProteinCharSequence(final GeneticCode geneticCode,final RNASequence cDNA)
			{
			this.geneticCode=geneticCode;
			this.cDNA=cDNA;
			}
		
		@Override
		public char charAt(int i)
			{
			return geneticCode.translate(
				cDNA.charAt(i*3+0),
				cDNA.charAt(i*3+1),
				cDNA.charAt(i*3+2));
			}	
		
		@Override
		public int length()
			{
			return this.cDNA.length()/3;
			}
	}


	


	private void backLocate(
		final PrintStream out,
		final KnownGene gene,
		final String geneName,
		char aa1,char aa2,
		int peptidePos1
		) throws IOException
		{
		
		final GeneticCode geneticCode=getGeneticCodeByChromosome(gene.getChromosome());
		RNASequence wildRNA=null;
		ProteinCharSequence wildProt=null;
		
	        		
	        		
		if(genomicSeq==null ||
	               !gene.getChromosome().equals(genomicSeq.getChrom()) 
	               )
        	{
        	LOG.info("fetch genome");
        	this.genomicSeq= new GenomicSequence(this.indexedFastaSequenceFile, gene.getContig());
        	}
        	
	        		
	        		
	        		
	     if(gene.isPositiveStrand())
    		{    		
    		int exon_index=0;
    		while(exon_index< gene.getExonCount())
    			{
    			for(int i= gene.getExonStart(exon_index);
    					i< gene.getExonEnd(exon_index);
    					++i)
    				{
    				if(i< gene.getCdsStart()) continue;
    				if(i>=gene.getCdsEnd()) break;
					
					if(wildRNA==null)
						{
						wildRNA=new RNASequence(genomicSeq,'+');
						}

    				wildRNA.genomicPositions.add(i);
    				
    				
    				
    				if(wildRNA.length()%3==0 && wildRNA.length()>0 && wildProt==null)
        				{
        				wildProt=new ProteinCharSequence(geneticCode,wildRNA);
        				}
    				}
    			++exon_index;
    			}
    		
    		
    		
    		}
	   else // reverse orientation
    		{
    		int exon_index = gene.getExonCount()-1;
    		while(exon_index >=0)
    			{
    			for(int i= gene.getExonEnd(exon_index)-1;
    				    i>= gene.getExonStart(exon_index);
    				--i)
    				{
    				if(i>= gene.getCdsEnd()) continue;
    				if(i<  gene.getCdsStart()) break;
    				
    				if(wildRNA==null)
						{
						wildRNA=new RNASequence(genomicSeq,'-');
						}
    				
    				
    				
    				wildRNA.genomicPositions.add(i);
    				if( wildRNA.length()%3==0 &&
    					wildRNA.length()>0 &&
    					wildProt==null)
        				{
        				wildProt=new ProteinCharSequence(geneticCode,wildRNA);
        				}
    				
    				}
    			--exon_index;
    			}

    		}//end of if reverse
	        		
	     if(wildProt==null)
	    	 {
	    	 stderr().println("#no protein found for transcript:"+gene.getName());
	    	 return;
	    	 }
	    int peptideIndex0= peptidePos1-1;
        if(peptideIndex0 >=wildProt.length())
        	{
        	out.println("#index out of range for :"+gene.getName()+" petide length="+wildProt.length());
        	return;
        	}
    
        if(wildProt.charAt(peptideIndex0)!=aa1)
        	{
        	out.println("##Warning ref aminod acid for "+gene.getName() +"  ["+peptidePos1+"] is not the same ("+wildProt.charAt(peptideIndex0)+"/"+aa1+")");
        	}
        else
        	{
        	out.println("##"+gene.getName());
        	}
        int indexesInRNA[]=new int[]{
        	0+ peptideIndex0*3,
        	1+ peptideIndex0*3,
        	2+ peptideIndex0*3
        	};
        final String wildCodon=""
        		+ wildRNA.charAt(indexesInRNA[0])
        		+ wildRNA.charAt(indexesInRNA[1])
        		+ wildRNA.charAt(indexesInRNA[2])
        		;
        /* 2015 : adding possible mut codons */
        final Set<String> possibleAltCodons = new HashSet<>();
        final char bases[]=new char[]{'A','C','G','T'};
        for(int codon_pos=0;codon_pos<3;++codon_pos)
        	{
        	StringBuilder sb=new StringBuilder(wildCodon);
        	for(char mutBase:bases)
        		{
        		sb.setCharAt(codon_pos, mutBase);
        		if(geneticCode.translate(sb.charAt(0), sb.charAt(1), sb.charAt(2))==Character.toUpperCase(aa2))
        			{
        			possibleAltCodons.add(sb.toString());
        			}
        		}
        	}
        
        for(int indexInRna: indexesInRNA)
        	{
        	out.print(geneName);
        	out.print('\t');
        	out.print(aa1);
        	out.print('\t');
        	out.print(peptidePos1);
        	out.print('\t');
        	out.print(aa2);
        	out.print('\t');
        	out.print(gene.getName());
        	out.print('\t');
        	out.print(gene.getStrand()==Strand.NEGATIVE?"-":"+");
        	out.print('\t');
        	out.print(wildProt.charAt(peptideIndex0));
        	out.print('\t');
        	out.print(indexInRna);
        	out.print('\t');
        	out.print(wildCodon);
        	out.print('\t');
        	if(possibleAltCodons.isEmpty())
        		{
        		out.print('.');
        		}
        	else
        		{
        		boolean first=true;
        		for(String mutCodon:possibleAltCodons)
        			{
        			if(!first) out.print('|');
        			first=false;
        			out.print(mutCodon);
        			}
        		}
        	out.print('\t');
        	out.print(wildRNA.charAt(indexInRna));
        	out.print('\t');
        	out.print(gene.getChromosome());
        	out.print('\t');
        	out.print(wildRNA.genomicPositions.get(indexInRna));
        	out.print('\t');
        	String exonName=null;
        	for(KnownGene.Exon exon : gene.getExons())
				{
				int genome=wildRNA.genomicPositions.get(indexInRna);
				if(exon.getStart()<=genome && genome< exon.getEnd())
					{
					exonName=exon.getName();
					break;
					}
				}
        	out.print(exonName);
        	if(this.printSequences)
        		{
        		String s=wildRNA.toString();
        		out.print('\t');
            	out.print(s.substring(0,indexInRna)+"["+s.charAt(indexInRna)+"]"+(indexInRna+1<s.length()?s.substring(indexInRna+1):""));
            	s=wildProt.toString();
            	out.print('\t');
            	out.print(s.substring(0,peptideIndex0)+"["+aa1+"/"+aa2+"/"+wildProt.charAt(peptideIndex0)+"]"+(peptideIndex0+1<s.length()?s.substring(peptideIndex0+1):""));
        		}
        	out.println();
        	}
		}

	
	private static char complement(char c)
		{
		switch(c)
			{
			case 'A':case 'a': return 'T';
			case 'T':case 't': return 'A';
			case 'G':case 'g': return 'C';
			case 'C':case 'c': return 'G';
			default:throw new IllegalArgumentException(""+c);
			}
		}
	
	private void run(PrintStream out,LineIterator in) throws IOException
		{
		while(in.hasNext())
			{
			String line=in.next();
			if(line.startsWith("#") || line.trim().isEmpty()) continue;
			int n=line.indexOf('\t');
			if(n==0 || n==-1) throw new IOException("Bad line. No tab found in "+line);
			String geneName=line.substring(0,n).trim();
			if(geneName.isEmpty()) throw new IOException("Bad line. No gene in "+geneName);
			String mut=line.substring(n+1).trim();
			if(!mut.matches("[A-Za-z\\*][0-9]+[A-Za-z\\*]")) throw new IOException("Bad mutation  in "+line);
			char aa1= mut.substring(0,1).toUpperCase().charAt(0);
			char aa2= mut.substring(mut.length()-1).toUpperCase().charAt(0);
			int position1=Integer.parseInt(mut.substring(1,mut.length()-1));
			if(position1==0) throw new IOException("Bad position  in "+line);
			Set<String> kgIds= this.geneSymbol2kg.get(geneName.toUpperCase());
			if(kgIds==null || kgIds.isEmpty())
				{
				LOG.warn("No kgXref found for "+geneName);
				continue;
				}
			
			
			for(String kgId:kgIds)
				{
				KnownGene kg=this.knwonGenes.get(kgId);
				if(kg==null) continue;
				backLocate(out,kg, geneName, aa1, aa2, position1);
				}
			}
		}
	
	
	private void loadKnownGenesFromUri(String kgURI) throws IOException
		{
		if(this.indexedFastaSequenceFile.getSequenceDictionary()==null)
			{
			throw new JvarkitException.FastaDictionaryMissing("No sequence dictionary in "+this.indexedRefFile);
			}
		
		LOG.info("loading genes");
		final Set<String> unknown=new HashSet<String>();
		BufferedReader in=IOUtils.openURIForBufferedReading(kgURI);
		String line;
		final Pattern tab=Pattern.compile("[\t]");
		while((line=in.readLine())!=null)
			{
			if(line.isEmpty()) continue;
			final String tokens[]=tab.split(line);
			final KnownGene g=new KnownGene(tokens);
			final Interval rgn=new Interval(g.getContig(), g.getTxStart()+1, g.getTxEnd());
			if(this.indexedFastaSequenceFile.getSequenceDictionary().getSequence(rgn.getContig())==null)
				{
				if(!unknown.contains(g.getContig()))
					{
					LOG.warn("The reference doesn't contain chromosome "+g.getContig());
					unknown.add(g.getContig());
					}
				continue;
				}
			
			this.knwonGenes.put(g.getName(),g);
			}
		in.close();
		LOG.info("genes:"+this.knwonGenes.size());
		}
	
	private void loadkgXRefFromUri(String kgURI) throws IOException
		{
		
		LOG.info("loading "+kgURI);
		final BufferedReader in=IOUtils.openURIForBufferedReading(kgURI);
		String line;
		final Pattern tab=Pattern.compile("[\t]");
		while((line=in.readLine())!=null)
			{
			if(line.isEmpty()) continue;
			final String tokens[]=tab.split(line);
			final String kgId=tokens[0];
			if(!this.knwonGenes.containsKey(kgId)) continue;
			final String geneSymbol=tokens[4];
			Set<String> kglist= this.geneSymbol2kg.get(geneSymbol.toUpperCase());
			if(kglist==null)
				{
				kglist=new HashSet<String>();
				geneSymbol2kg.put(geneSymbol.toUpperCase(),kglist);
				}
			kglist.add(kgId);//kgID
			}
		in.close();
		LOG.info("kgxref:"+geneSymbol2kg.size());
		}

	
	@Override
	public int doWork(List<String> args) {
		PrintStream out=null;
		try {			
		
			if(this.indexedRefFile==null)
				{
				throw new JvarkitException.CommandLineError("Reference file was not provided");
				}
			this.indexedFastaSequenceFile=new IndexedFastaSequenceFile(this.indexedRefFile);
			
			if(knownGeneURI==null)
				{
				throw new JvarkitException.CommandLineError("Undefined knwonGeneURI");
				}
			
			if(kgXRef==null)
				{
				throw new JvarkitException.CommandLineError("Undefined kgXref");
				}
			this.loadKnownGenesFromUri(knownGeneURI);
			this.loadkgXRefFromUri(kgXRef);

			
			
			out = this.openFileOrStdoutAsPrintStream(this.outputFile);
			
			out.print("#User.Gene");
        	out.print('\t');
        	out.print("AA1");
        	out.print('\t');
        	out.print("petide.pos.1");
        	out.print('\t');
        	out.print("AA2");
        	out.print('\t');
        	out.print("knownGene.name");
        	out.print('\t');
        	out.print("knownGene.strand");
        	out.print('\t');
        	out.print("knownGene.AA");
        	out.print('\t');
        	out.print("index0.in.rna");
        	out.print('\t');
        	out.print("wild.codon");
        	out.print('\t');
        	out.print("potential.var.codons");
        	out.print('\t');
        	out.print("base.in.rna");
        	out.print('\t');
        	out.print("chromosome");
        	out.print('\t');
        	out.print("index0.in.genomic");
        	out.print('\t');
        	out.print("exon");
        	if(this.printSequences)
        		{
        		out.print('\t');
            	out.print("mRNA");
            	out.print('\t');
            	out.print("protein");
        		}
        	out.println();
			if(args.isEmpty())
				{
				LOG.info("reading from stdin");
				LineIterator in=IOUtils.openStdinForLineIterator();
				this.run(out,in);
				CloserUtil.close(in);
				}
			else
				{
				for(String filename:args)
					{
					LOG.info("reading from "+filename);
					LineIterator in=IOUtils.openURIForLineIterator(filename);
					this.run(out,in);
					CloserUtil.close(in);
					}
				}
			return 0;
			}
		catch (final Exception e) {
			LOG.severe(e);
			return -1;
			}
		finally
			{
			CloserUtil.close(this.indexedFastaSequenceFile);
			this.indexedFastaSequenceFile=null;
			CloserUtil.close(out);
			}	
		}
		
	public static void main(String[] args)
		{
		new BackLocate().instanceMainWithExit(args);
		}
	}
