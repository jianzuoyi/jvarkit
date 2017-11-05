# Biostar139647

Convert alignment in Fasta/Clustal format to SAM/BAM file


## Usage

```
Usage: biostar139647 [options] Files
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
    -R, --refname
      reference name. Optional
      Default: chrUn
    --samoutputformat
      Sam output format.
      Default: TypeImpl{name='SAM', fileExtension='sam', indexExtension='null'}
    --version
      print version and exit

```


## See also in Biostars

 * [https://www.biostars.org/p/139647](https://www.biostars.org/p/139647)


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
$ make biostar139647
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

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/biostar/Biostar139647.java
](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/biostar/Biostar139647.java
)
## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **biostar139647** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

 
## Example

```bash
$ curl -sL "https://raw.githubusercontent.com/suryasaha/Pred_cutoff/60a6f980c9940dfb6e381c5394918f27cb14564f/data/Xylella-RpoH.aln" |\
  java -jar dist-1.128/biostar139647.jar

@HD	VN:1.4	SO:unsorted
@SQ	SN:chrUn	LN:42
@PG	ID:0	VN:3a0c4ccb05e7492382e00328ac60951f215d9400	CL:(empty)	PN:Biostar139647
1	0	chrUn	1	60	42M	*	0	0	CATACTTGGTCATCGGTCGTGTCCTTGAAAGTGACTTGTTAA	*
2	0	chrUn	1	60	42M	*	0	0	TCTCTGAACCCCCTTGAAACCCCTACACTCAGCCATATATGC	*
3	0	chrUn	1	60	42M	*	0	0	TACCTTCGGGTCCTTGAAAATAGCGTCGCCGTGCTTATCTGT	*
4	0	chrUn	1	60	5M2D35M	*	0	0	TTGACAGCCGCTTGAGCAGGCGTCGGTCATCCCCACATTC	*
5	0	chrUn	1	60	18M1D9M1D13M	*	0	0	ATGCCTGGGTGGCTTGAAAGCTGGCGGCTTGCCCACATAC	*
6	0	chrUn	1	60	20M1D21M	*	0	0	TCAGTTTTATCGCTTGATATTCACTGAGACTGGCCACACAT	*

```

```
$ curl -sL "https://raw.github.com/biopython/biopython/master/Tests/Clustalw/opuntia.aln" 2> /dev/null | java -jar dist-1.128/biostar139647.jar 
@HD	VN:1.4	SO:unsorted
@SQ	SN:chrUn	LN:156
@PG	ID:0	VN:3a0c4ccb05e7492382e00328ac60951f215d9400	CL:(empty)	PN:Biostar139647
gi|6273285|gb|AF191659.1|AF191	0	chrUn	1	60	56M10D90M	*	0	0	TATACATTAAAGAAGGGGGATGCGGATAAATGGAAAGGCGAAAGAAAGAATATATAATATATTTCA
AATTTCCTTATATACCCAAATATAAAAATATCTAATAAATTAGATGAATATCAAAGAATCCATTGATTTAGTGTACCAGA*
gi|6273284|gb|AF191658.1|AF191	0	chrUn	1	60	58M8D90M	*	0	0	TATACATTAAAGAAGGGGGATGCGGATAAATGGAAAGGCGAAAGAAAGAATATATATAATATATTT
CAAATTTCCTTATATACCCAAATATAAAAATATCTAATAAATTAGATGAATATCAAAGAATCTATTGATTTAGTGTACCAGA	*
gi|6273289|gb|AF191663.1|AF191	0	chrUn	1	60	60M6D90M	*	0	0	TATACATTAAAGGAGGGGGATGCGGATAAATGGAAAGGCGAAAGAAAGAATATATATATAATATAT
TTCAAATTCCCTTATATATCCAAATATAAAAATATCTAATAAATTAGATGAATATCAAAGAATCTATTGATTTAGTATACCAGA	*
gi|6273291|gb|AF191665.1|AF191	0	chrUn	1	60	156M	*	0	0	TATACATTAAAGGAGGGGGATGCGGATAAATGGAAAGGCGAAAGAAAGAATATATATATATATATAATATATTT
CAAATTCCCTTATATATCCAAATATAAAAATATCTAATAAATTAGATGAATATCAAAGAATCTATTGATTTAGTGTACCAGA	*
gi|6273287|gb|AF191661.1|AF191	0	chrUn	1	60	56M10D90M	*	0	0	TATACATTAAAGAAGGGGGATGCGGATAAATGGAAAGGCGAAAGAAAGAATATATAATATATTTCA
AATTTCCTTATATATCCAAATATAAAAATATCTAATAAATTAGATGAATATCAAAGAATCTATTGATTTAGTGTACCAGA*
gi|6273286|gb|AF191660.1|AF191	0	chrUn	1	60	56M10D90M	*	0	0	TATACATAAAAGAAGGGGGATGCGGATAAATGGAAAGGCGAAAGAAAGAATATATAATATATTTAT
AATTTCCTTATATATCCAAATATAAAAATATCTAATAAATTAGATGAATATCAAAGAATCTATTGATTTAGTGTACCAGA*
gi|6273290|gb|AF191664.1|AF191	0	chrUn	1	60	60M6D90M	*	0	0	TATACATTAAAGGAGGGGGATGCGGATAAATGGAAAGGCGAAAGAAAGAATATATATATAATATAT
TTCAAATTCCCTTATATATCCAAATATAAAAATATCTAATAAATTAGATGAATATCAAAGAATCTATTGATTTAGTGTACCAGA	*
```


