# SamFixCigar

Fix Cigar String in SAM replacing 'M' by 'X' or '='


## Usage

```
Usage: samfixcigar [options] Files
  Options:
    --bamcompression
      Compression Level.
      Default: 5
    -h, --help
      print help and exit
    --helpFormat
      What kind of help
      Possible Values: [usage, markdown, xml]
    -o, --output
      Output file. Optional . Default: stdout
  * -r, -R, --reference
      Indexed fasta Reference file. This file must be indexed with samtools 
      faidx and with picard CreateSequenceDictionary
    --samoutputformat
      Sam output format.
      Default: TypeImpl{name='SAM', fileExtension='sam', indexExtension='null'}
    --version
      print version and exit

```


## Keywords

 * sam
 * bam
 * cigar


## Compilation

### Requirements / Dependencies

* java compiler SDK 1.8 http://www.oracle.com/technetwork/java/index.html (**NOT the old java 1.7 or 1.6**) . Please check that this java is in the `${PATH}`. Setting JAVA_HOME is not enough : (e.g: https://github.com/lindenb/jvarkit/issues/23 )
* GNU Make >= 3.81
* curl/wget
* git
* xsltproc http://xmlsoft.org/XSLT/xsltproc2.html (tested with "libxml 20706, libxslt 10126 and libexslt 815")


### Download and Compile

```bash
$ git clone "https://github.com/lindenb/jvarkit.git"
$ cd jvarkit
$ make samfixcigar
```

The *.jar libraries are not included in the main jar file, so you shouldn't move them (https://github.com/lindenb/jvarkit/issues/15#issuecomment-140099011 ).
The required libraries will be downloaded and installed in the `dist` directory.

### edit 'local.mk' (optional)

The a file **local.mk** can be created edited to override/add some definitions.

For example it can be used to set the HTTP proxy:

```
http.proxy.host=your.host.com
http.proxy.port=124567
```
## Source code 

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/samfixcigar/SamFixCigar.java
](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/samfixcigar/SamFixCigar.java
)
## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **samfixcigar** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)



### Example


the input file


```
$ cat toy.sam

@SQ     SN:ref  LN:45
@SQ     SN:ref2 LN:40
r001    163     ref     7       30      8M4I4M1D3M      =       37      39      TTAGATAAAGAGGATACTG     *       XX:B:S,12561,2,20,112
r002    0       ref     9       30      1S2I6M1P1I1P1I4M2I      *       0       0       AAAAGATAAGGGATAAA       *
r003    0       ref     9       30      5H6M    *       0       0       AGCTAA  *
r004    0       ref     16      30      6M14N1I5M       *       0       0       ATAGCTCTCAGC    *
r003    16      ref     29      30      6H5M    *       0       0       TAGGC   *
r001    83      ref     37      30      9M      =       7       -39     CAGCGCCAT       *
x1      0       ref2    1       30      20M     *       0       0       aggttttataaaacaaataa    ????????????????????
x2      0       ref2    2       30      21M     *       0       0       ggttttataaaacaaataatt   ?????????????????????
x3      0       ref2    6       30      9M4I13M *       0       0       ttataaaacAAATaattaagtctaca      ??????????????????????????
x4      0       ref2    10      30      25M     *       0       0       CaaaTaattaagtctacagagcaac       ?????????????????????????
x5      0       ref2    12      30      24M     *       0       0       aaTaattaagtctacagagcaact        ????????????????????????
x6      0       ref2    14      30      23M     *       0       0       Taattaagtctacagagcaacta ???????????????????????

```


processing with samfixcigar


```
$ java -jar dist/samfixcigar.jar \
     -r samtools-0.1.19/examples/toy.fa \
     samtools-0.1.19/examples/toy.sam
@HD     VN:1.4  SO:unsorted
@SQ     SN:ref  LN:45
@SQ     SN:ref2 LN:40
r001    163     ref     7       30      8=4I4=1D3=      =       37      39      TTAGATAAAGAGGATACTG     *       XX:B:S,12561,2,20,112
r002    0       ref     9       30      1S2I6=1P1I1P1I1X1=2X2I  *       0       0       AAAAGATAAGGGATAAA       *
r003    0       ref     9       30      2=1X3=  *       0       0       AGCTAA  *
r004    0       ref     16      30      6=14N1I5=       *       0       0       ATAGCTCTCAGC    *
r003    16      ref     29      30      5=      *       0       0       TAGGC   *
r001    83      ref     37      30      9=      =       7       -39     CAGCGCCAT       *
x1      0       ref2    1       30      16=1X3= *       0       0       AGGTTTTATAAAACAAATAA    ????????????????????
x2      0       ref2    2       30      15=1X3=1X1=     *       0       0       GGTTTTATAAAACAAATAATT   ?????????????????????
x3      0       ref2    6       30      9=4I13= *       0       0       TTATAAAACAAATAATTAAGTCTACA      ??????????????????????????
x4      0       ref2    10      30      1X3=1X20=       *       0       0       CAAATAATTAAGTCTACAGAGCAAC       ?????????????????????????
x5      0       ref2    12      30      2=1X21= *       0       0       AATAATTAAGTCTACAGAGCAACT        ????????????????????????
x6      0       ref2    14      30      1X22=   *       0       0       TAATTAAGTCTACAGAGCAACTA ???????????????????????
```

### Usage in the literature

This tool was cited in Extensive sequencing of seven human genomes to characterize benchmark reference materials Sci Data. 2016; 3: 160025..

*/

@Program(name="samfixcigar",
	description="Fix Cigar String in SAM replacing 'M' by 'X' or '='",
	keywords={"sam","bam","cigar"}
		)
public class SamFixCigar extends Launcher
	{
	private static final Logger LOG = Logger.build(SamFixCigar.class).make();

	@Parameter(names={"-o","--output"},description=OPT_OUPUT_FILE_OR_STDOUT)
	private File outputFile = null;


	@Parameter(names={"-r","-R","--reference"},description=INDEXED_FASTA_REFERENCE_DESCRIPTION,required=true)
	private File faidx = null;
	
	@ParametersDelegate
	private WritingBamArgs writingBamArgs=new WritingBamArgs();

	private IndexedFastaSequenceFile indexedFastaSequenceFile=null;
	
	
	@Override
	public int doWork(List<String> args) {		
		if(this.faidx==null)
			{
			LOG.error("Reference was not specified.");
			return -1;
			}
		GenomicSequence genomicSequence=null;

		SamReader sfr=null;
		SAMFileWriter sfw=null;
		try
			{
			this.indexedFastaSequenceFile=new IndexedFastaSequenceFile(faidx);
			sfr = openSamReader(oneFileOrNull(args));
			final SAMFileHeader header=sfr.getFileHeader();
			sfw = this.writingBamArgs.
					setReferenceFile(this.faidx).
					openSAMFileWriter(outputFile,header, true);
			final SAMSequenceDictionaryProgress progress= new SAMSequenceDictionaryProgress(header);
			final List<CigarElement> newCigar=new ArrayList<CigarElement>();
			final SAMRecordIterator iter=sfr.iterator();
			while(iter.hasNext())
				{
				final SAMRecord rec=progress.watch(iter.next());
				Cigar cigar=rec.getCigar();
				byte bases[]=rec.getReadBases();
				if( rec.getReadUnmappedFlag() ||
					cigar==null ||
					cigar.getCigarElements().isEmpty() ||
					bases==null)
					{
					sfw.addAlignment(rec);
					continue;
					}
				
				if(genomicSequence==null ||
					genomicSequence.getSAMSequenceRecord().getSequenceIndex()!=rec.getReferenceIndex())
					{
					genomicSequence=new GenomicSequence(indexedFastaSequenceFile, rec.getReferenceName());
					}
				
				newCigar.clear();
				int refPos1=rec.getAlignmentStart();
				int readPos0=0;
				
				for(final CigarElement ce:cigar.getCigarElements())
					{
					final CigarOperator op = ce.getOperator();
					if(op.equals(CigarOperator.M))
						{
						for(int i=0;i< ce.getLength();++i)
    		    			{
							char c1=Character.toUpperCase((char)bases[readPos0]);
							char c2=Character.toUpperCase(refPos1-1< genomicSequence.length()?genomicSequence.charAt(refPos1-1):'*');
							
							if(c2=='N' || c1==c2)
								{
								newCigar.add(new CigarElement(1, CigarOperator.EQ));
								}
							else
								{
								newCigar.add(new CigarElement(1, CigarOperator.X));
								}
    						refPos1++;
    						readPos0++;
		    				}
						}
					else
						{
						newCigar.add(ce);
						if(op.consumesReadBases()) readPos0+=ce.getLength();	
						if(op.consumesReferenceBases()) refPos1+=ce.getLength();	
						}
					}
				
				int i=0;
				while(i< newCigar.size())
					{
					final CigarOperator op1 = newCigar.get(i).getOperator();
					final int length1 = newCigar.get(i).getLength();
					
					if( i+1 <  newCigar.size() &&
						newCigar.get(i+1).getOperator()==op1)
						{
						final CigarOperator op2= newCigar.get(i+1).getOperator();
						int length2=newCigar.get(i+1).getLength();

						 newCigar.set(i,new CigarElement(length1+length2, op2));
						 newCigar.remove(i+1);
						}
					else
						{
						++i;
						}
					}
				cigar=new Cigar(newCigar);
				//info("changed "+rec.getCigarString()+" to "+newCigarStr+" "+rec.getReadName()+" "+rec.getReadString());
				rec.setCigar(cigar);
				
				sfw.addAlignment(rec);
				}
			progress.finish();
			return RETURN_OK;
			}
		catch(Exception err)
			{
			LOG.error(err);
			return -1;
			}
		finally
			{
			CloserUtil.close(this.indexedFastaSequenceFile);
			CloserUtil.close(sfr);
			CloserUtil.close(sfw);
			}
		}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SamFixCigar().instanceMainWithExit(args);

	}

}

