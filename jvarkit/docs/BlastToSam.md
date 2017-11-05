# BlastToSam

Convert a **BLASTN-XML** input to SAM


## Usage

```
Usage: blast2sam [options] Files
  Options:
    --bamcompression
      Compression Level.
      Default: 5
    -p, --expect_size
      input is an interleaved list of sequences forward and reverse 
      (paired-ends). 0: not interleaved
      Default: 0
    -h, --help
      print help and exit
    --helpFormat
      What kind of help
      Possible Values: [usage, markdown, xml]
    -o, --output
      Output file. Optional . Default: stdout
    -r, --reference
      Indexed fasta Reference
    --samoutputformat
      Sam output format.
      Default: TypeImpl{name='SAM', fileExtension='sam', indexExtension='null'}
    --version
      print version and exit

```


## Keywords

 * sam
 * blast


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
$ make blast2sam
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

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/blast2sam/BlastToSam.java
](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/blast2sam/BlastToSam.java
)
## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **blast2sam** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)






### Example

The following Makefile downloads a reference , generates some FASTQs, align them with blastn and convert it to SAM:


```
BLASTN=/commun/data/packages/ncbi/ncbi-blast-2.2.28+/bin/blastn
SAMTOOLS=/commun/data/packages/samtools-0.1.19
JVARKIT=/home/lindenb/src/jvarkit-git/dist/
SHELL=/bin/bash
.PHONY:all reads clean
all: out.sam



out.sam: ref.fa ref.fa.fai out.read1.fq out.read2.fq
	paste \
		<(cat out.read1.fq | paste - - - - | cut -f 1,2 ) \
		<(cat out.read2.fq | paste - - - - | cut -f 1,2 ) |\
	tr "\t" "\n" |\
	sed 's/^@/>/' |\
	${BLASTN} -subject ref.fa -dust no -outfmt 5 | \
	java -jar ${JVARKIT}/blast2sam.jar -r ref.fa -p 500  |\
	${SAMTOOLS}/samtools view -Sh -f 2 - > $@
	
reads: out.read1.fq out.read2.fq
out.read1.fq out.read2.fq: ref.fa ref.fa.fai
	${SAMTOOLS}/misc/wgsim  -d 100 -N 500 -1 50 -2 50   $< out.read1.fq out.read2.fq > /dev/null

ref.fa:
	curl -k -o $@ "https://raw.github.com/lindenb/genomehub/master/data/rotavirus/rf/rf.fa"

ref.fa.fai: ref.fa
	${SAMTOOLS}/samtools faidx $<

clean:
	rm -f ref.fa.fai ref.fa out.sam 

```





### Output



```
@HD	VN:1.4	SO:unsorted
@SQ	SN:RF01	LN:3302
@SQ	SN:RF02	LN:2687
@SQ	SN:RF03	LN:2592
@SQ	SN:RF04	LN:2362
@SQ	SN:RF05	LN:1579
@SQ	SN:RF06	LN:1356
@SQ	SN:RF07	LN:1074
@SQ	SN:RF08	LN:1059
@SQ	SN:RF09	LN:1062
@SQ	SN:RF10	LN:751
@SQ	SN:RF11	LN:666
@RG	ID:g1	LB:blast	DS:blast	SM:blast
@PG	ID:0	PN:blastn	VN:BLASTN_2.2.28+
@PG	ID:1	PN:com.github.lindenb.jvarkit.tools.blast2sam.BlastToSam	PP:0	VN:3365d9b714aa43d4fba44bfbf102a179a1f1573fCL:-r ref.fa -p 500
RF01_445_573_0:0:0_0:0:0_0/1	83	RF01	524	40	50=	=	445	-30	GTGCCTTGGTACACCATATTTATTTACTGTTGAAGCTACTATAGTGAATA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_445_573_0:0:0_0:0:0_0/2	163	RF01	445	40	50=	=	524	30	AATGCAGTTATGTTCTGGTTGGAAAAACATGAAAATGACGTTGCTGAAAA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_1193_1294_1:0:0_1:0:0_1/1	83	RF01	1245	40	38=1X11=	=	1193	-3	CCATTACATGCATATTCTTTTTAGTCGAAAAAATTGTCATTCTACCAAAT	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_1193_1294_1:0:0_1:0:0_1/2	163	RF01	1193	40	4=1X45=	=	1245	3	CTGGATTACTATCAATGTCATCAGCGTCGAATGGTGAATCAAGACAACTA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_638_718_1:0:0_0:0:0_2/1	83	RF01	669	40	50=	=	638	18	ATGACAGTACTATCAGTTCTCTCGCAATTAAATAATCTTCATGAGAAAAA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_638_718_1:0:0_0:0:0_2/2	163	RF01	638	40	4=1X45=	=	669	-18	CAAAATCTTCAATTGAAATGCTGATGTCAGTTTTTTCTCATGAAGATTAT	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_1404_1584_0:0:0_2:0:0_3/1	99	RF01	1404	40	50=	=	1535	179	ATTTATCTTACCATATGAATATTTCATAGCACAACATGCTGTAGTTGAAA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_1404_1584_0:0:0_2:0:0_3/2	147	RF01	1535	40	1S42=1X6=	=	1404	-179	NGACACGTCTGTATATAGTACCATAGAGTTATTAGATAAAAAGGGTGTAA	#JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:86.0662	BE:f:1.62562e-21	RG:Z:g1	NM:i:0	BS:f:46
RF01_284_373_0:0:0_1:0:0_5/1	99	RF01	284	40	50=	=	324	89	TAGTAAAATATGCAAAAGGTAAGCCGCTAGAAGCAGATTTGACAGTGAAT	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_284_373_0:0:0_1:0:0_5/2	147	RF01	324	40	8=1X41=	=	284	-89	AAAGTTCATATGTTATCTTGTTATTTTCATAATCCAACTCATTCACTGTC	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_1704_1823_1:0:0_0:0:0_7/1	83	RF01	1774	40	50=	=	1704	-21	ATTGAATTCGCTGCTTTCGTCTGCTTCTCTCCTGACGCTACAGCCCCATA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_1704_1823_1:0:0_0:0:0_7/2	163	RF01	1704	40	5=1X44=	=	1774	21	ACAGAGGCAAATTAATCTAATGGATTCATACGTTCAAATACCAGATGGTA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_689_741_1:0:0_1:0:0_8/1	83	RF01	692	40	19=1X30=	=	689	46	TGCCAGAGTCGATCTATTATAATATGACAGTACTATCAGTTCTCTCGCAA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_689_741_1:0:0_1:0:0_8/2	163	RF01	689	40	30=1X19=	=	692	-46	TAATTGCGAGAGAACTGATAGTACTGTCATCTTCTAATAGATCGACTCTG	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_532_688_0:0:0_1:0:0_9/1	99	RF01	532	40	50=	=	639	156	ATAGTAGCTTCAACAGTAAATAAATATGGTGTACCAAGGCACAACGCGAA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
(...)

```






### Example

The following Makefile downloads a reference , generates some FASTQs, align them with blastn and convert it to SAM:

```
BLASTN=/commun/data/packages/ncbi/ncbi-blast-2.2.28+/bin/blastn
SAMTOOLS=/commun/data/packages/samtools-0.1.19
JVARKIT=/home/lindenb/src/jvarkit-git/dist/
SHELL=/bin/bash
.PHONY:all reads clean
all: out.sam



out.sam: ref.fa ref.fa.fai out.read1.fq out.read2.fq
	paste \
		<(cat out.read1.fq | paste - - - - | cut -f 1,2 ) \
		<(cat out.read2.fq | paste - - - - | cut -f 1,2 ) |\
	tr "\t" "\n" |\
	sed 's/^@/>/' |\
	${BLASTN} -subject ref.fa -dust no -outfmt 5 | \
	java -jar ${JVARKIT}/blast2sam.jar -r ref.fa -p 500  |\
	${SAMTOOLS}/samtools view -Sh -f 2 - > $@
	
reads: out.read1.fq out.read2.fq
out.read1.fq out.read2.fq: ref.fa ref.fa.fai
	${SAMTOOLS}/misc/wgsim  -d 100 -N 500 -1 50 -2 50   $< out.read1.fq out.read2.fq > /dev/null

ref.fa:
	curl -k -o $@ "https://raw.github.com/lindenb/genomehub/master/data/rotavirus/rf/rf.fa"

ref.fa.fai: ref.fa
	${SAMTOOLS}/samtools faidx $<

clean:
	rm -f ref.fa.fai ref.fa out.sam 

```



### Output

```
@HD	VN:1.4	SO:unsorted
@SQ	SN:RF01	LN:3302
@SQ	SN:RF02	LN:2687
@SQ	SN:RF03	LN:2592
@SQ	SN:RF04	LN:2362
@SQ	SN:RF05	LN:1579
@SQ	SN:RF06	LN:1356
@SQ	SN:RF07	LN:1074
@SQ	SN:RF08	LN:1059
@SQ	SN:RF09	LN:1062
@SQ	SN:RF10	LN:751
@SQ	SN:RF11	LN:666
@RG	ID:g1	LB:blast	DS:blast	SM:blast
@PG	ID:0	PN:blastn	VN:BLASTN_2.2.28+
@PG	ID:1	PN:com.github.lindenb.jvarkit.tools.blast2sam.BlastToSam	PP:0	VN:3365d9b714aa43d4fba44bfbf102a179a1f1573f	CL:-r ref.fa -p 500
RF01_445_573_0:0:0_0:0:0_0/1	83	RF01	524	40	50=	=	445	-30	GTGCCTTGGTACACCATATTTATTTACTGTTGAAGCTACTATAGTGAATA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_445_573_0:0:0_0:0:0_0/2	163	RF01	445	40	50=	=	524	30	AATGCAGTTATGTTCTGGTTGGAAAAACATGAAAATGACGTTGCTGAAAA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_1193_1294_1:0:0_1:0:0_1/1	83	RF01	1245	40	38=1X11=	=	1193	-3	CCATTACATGCATATTCTTTTTAGTCGAAAAAATTGTCATTCTACCAAAT	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_1193_1294_1:0:0_1:0:0_1/2	163	RF01	1193	40	4=1X45=	=	1245	3	CTGGATTACTATCAATGTCATCAGCGTCGAATGGTGAATCAAGACAACTA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_638_718_1:0:0_0:0:0_2/1	83	RF01	669	40	50=	=	638	18	ATGACAGTACTATCAGTTCTCTCGCAATTAAATAATCTTCATGAGAAAAA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_638_718_1:0:0_0:0:0_2/2	163	RF01	638	40	4=1X45=	=	669	-18	CAAAATCTTCAATTGAAATGCTGATGTCAGTTTTTTCTCATGAAGATTAT	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_1404_1584_0:0:0_2:0:0_3/1	99	RF01	1404	40	50=	=	1535	179	ATTTATCTTACCATATGAATATTTCATAGCACAACATGCTGTAGTTGAAA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_1404_1584_0:0:0_2:0:0_3/2	147	RF01	1535	40	1S42=1X6=	=	1404	-179	NGACACGTCTGTATATAGTACCATAGAGTTATTAGATAAAAAGGGTGTAA	#JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:86.0662	BE:f:1.62562e-21	RG:Z:g1	NM:i:0	BS:f:46
RF01_284_373_0:0:0_1:0:0_5/1	99	RF01	284	40	50=	=	324	89	TAGTAAAATATGCAAAAGGTAAGCCGCTAGAAGCAGATTTGACAGTGAAT	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_284_373_0:0:0_1:0:0_5/2	147	RF01	324	40	8=1X41=	=	284	-89	AAAGTTCATATGTTATCTTGTTATTTTCATAATCCAACTCATTCACTGTC	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_1704_1823_1:0:0_0:0:0_7/1	83	RF01	1774	40	50=	=	1704	-21	ATTGAATTCGCTGCTTTCGTCTGCTTCTCTCCTGACGCTACAGCCCCATA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
RF01_1704_1823_1:0:0_0:0:0_7/2	163	RF01	1704	40	5=1X44=	=	1774	21	ACAGAGGCAAATTAATCTAATGGATTCATACGTTCAAATACCAGATGGTA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_689_741_1:0:0_1:0:0_8/1	83	RF01	692	40	19=1X30=	=	689	46	TGCCAGAGTCGATCTATTATAATATGACAGTACTATCAGTTCTCTCGCAA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_689_741_1:0:0_1:0:0_8/2	163	RF01	689	40	30=1X19=	=	692	-46	TAATTGCGAGAGAACTGATAGTACTGTCATCTTCTAATAGATCGACTCTG	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:87.9128	BE:f:4.51982e-22	RG:Z:g1	NM:i:0	BS:f:47
RF01_532_688_0:0:0_1:0:0_9/1	99	RF01	532	40	50=	=	639	156	ATAGTAGCTTCAACAGTAAATAAATATGGTGTACCAAGGCACAACGCGAA	JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ	BB:f:93.4528	BE:f:9.71473e-24	RG:Z:g1	NM:i:0	BS:f:50
(...)

```




