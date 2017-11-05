# Biostar214299

Extract allele specific reads from bamfiles


## Usage

```
Usage: biostar214299 [options] Files
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
  * -p, --positions
      Position file. A Tab delimited file containing the following 4 column: 
      (1)chrom (2)position (3) allele A/T/G/C (4) sample name.
    --samoutputformat
      Sam output format.
      Default: TypeImpl{name='SAM', fileExtension='sam', indexExtension='null'}
    --version
      print version and exit

```


## Keywords

 * sam
 * bam
 * variant
 * snp



## See also in Biostars

 * [https://www.biostars.org/p/214299](https://www.biostars.org/p/214299)


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
$ make biostar214299
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

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/biostar/Biostar214299.java
](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/biostar/Biostar214299.java
)
## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **biostar214299** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)


The program removes all the existing read group and create some new one from the 'position file'.
For now, only simple alleles are supported.
Reads group are affected if a specific variant is found in the 'position file'.
If two samples share the same group, the read group is AMBIGOUS.
If the read is unmapped, the read group is UNMAPPED.
If no sample is affected to a read, the read group will be UNAFFECTED;
 
## Example

the positions file

```
$ cat positions.tsv
rotavirus       267     C       SAMPLE1
rotavirus       267     G       SAMPLE2
```

processing : 

```
$ java -jar dist/biostar214299.jar -p positions.tsv input.bam

@HD     VN:1.5  SO:coordinate
@SQ     SN:rotavirus    LN:1074
@RG     ID:UNAFFECTED   SM:UNAFFECTED   LB:UNAFFECTED
@RG     ID:UNMAPPED     SM:UNMAPPED     LB:UNMAPPED
@RG     ID:SAMPLE1      SM:SAMPLE1      LB:SAMPLE1
@RG     ID:SAMPLE2      SM:SAMPLE2      LB:SAMPLE2
@RG     ID:AMBIGOUS     SM:AMBIGOUS     LB:AMBIGOUS
(...)
rotavirus_237_744_6:0:0_3:0:0_29c       163     rotavirus       237     60      70M     =       675     508     ATCCGGCGTTAAATGGAAAGTTTCGGTGATCTATTAGAAATAGAAATTGGATGACTGATTCAAAAACGGT  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      MD:Z:3A19A1C1C1G31T8    RG:Z:SAMPLE1    NM:i:6  AS:i:41 XS:i:0
rotavirus_234_692_6:0:1_4:0:0_3ac       163     rotavirus       237     60      6S30M5I1M5D28M  =       623     456     TTGGTAATCAGGCGTTAAATGGAAAGTTTAGCTCAGGACAACGAAATAGAAATTGGATGACTGATTCTAA  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      MD:Z:31^TATTA28 RG:Z:SAMPLE2    NM:i:10 AS:i:37 XS:i:0
rotavirus_237_777_6:0:0_7:0:0_216       99      rotavirus       237     60      70M     =       708     541     ATCAGGGGTTAAATTGAAAGTTTAGCTCAGCTCTTAGACATAGAAATTGGATGACTGATTGTACAACGGT  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      MD:Z:6C7G17A5A21C2A6    RG:Z:SAMPLE1    NM:i:6  AS:i:40 XS:i:0
rotavirus_237_699_3:0:0_8:0:0_22f       163     rotavirus       237     60      70M     =       650     463     ATGAGGCGTTAAATGGAAAGTTTATCTCAGCTATTAGAAATAGCAATTGGATGACTGATTCTAAAACGGT  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      MD:Z:2C21G18A26 RG:Z:SAMPLE1    NM:i:3  AS:i:57 XS:i:0
(...)
rotavirus_311_846_10:0:0_11:0:0_3d7     141     *       0       0       *       *       0       0       AACTTAGATGAAGACGATCAAAACCTTAGAATGACTTTATGTTCTAAATGGCTCGACCCAAAGATGAGAG  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      RG:Z:UNMAPPED   AS:i:0  XS:i:0
rotavirus_85_600_7:0:0_9:0:0_3e0        77      *       0       0       *       *       0       0       AGCTGCAGTTGTTTCTGCTCCTTCAACATTAGAATTACTGGGTATTGAATATGATTCCAATGAAGTCTAT  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      RG:Z:UNMAPPED   AS:i:0  XS:i:0
rotavirus_85_600_7:0:0_9:0:0_3e0        141     *       0       0       *       *       0       0       TATTTCTCCTTAAGCCTGTGTTTTATTGCATCAAATCTTTTTTCAAACTGCTCATAACGAGATTTCCACT  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      RG:Z:UNMAPPED   AS:i:0  XS:i:0
```


