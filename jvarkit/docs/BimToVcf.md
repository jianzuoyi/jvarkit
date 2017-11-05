# BimToVcf

convert a .bim to a .vcf


## Usage

```
Usage: bim2vcf [options] Files
  Options:
    -h, --help
      print help and exit
    --helpFormat
      What kind of help
      Possible Values: [usage, markdown, xml]
    -o, --output
      Output file. Optional . Default: stdout
  * -R, --reference
      Indexed fasta Reference file. This file must be indexed with samtools 
      faidx and with picard CreateSequenceDictionary
    --version
      print version and exit

```

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
$ make bim2vcf
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

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/misc/BimToVcf.java
](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/misc/BimToVcf.java
)
## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **bim2vcf** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)





### Contig conversion

chromosome 23 is converted to X or chrX, chromosome 24 is converted to Y or chrY, chromosome 25 is ignored, chromosome 26 is converted to chrM or MT.


### Example



```
$ java -jar dist/bim2vcf.jar -R human_g1k_v37.fasta input.bim 

##fileformat=VCFv4.2
##INFO=<ID=MORGAN,Number=1,Type=Float,Description="Centimorgan">
##INFO=<ID=SVTYPE,Number=1,Type=String,Description="Variation type">
##contig=<ID=1,length=249250621,assembly=human_g1k_v37>
(...)
#CHROM  POS     ID      REF     ALT     QUAL    FILTER  INFO
1       12      rs73422 C       .       .       .       MORGAN=0.5224;SVTYPE=NOVARIATION
1       13      rs30315 G       A       .       .       MORGAN=0.530874;SVTYPE=SNV
1       14      rs14325 C       T       .       .       MORGAN=0.532596;SVTYPE=SNV
1       15      rs31319 A       G       .       .       MORGAN=0.532682;SVTYPE=SNV
1       16      rs954   C       T       .       .       MORGAN=0.537655;SVTYPE=SNV
1       17      rs62034 G       A       .       .       MORGAN=0.548645;SVTYPE=SNV
1       18      rs25996 A       G       .       .       MORGAN=0.575595;SVTYPE=SNV
1       19      rs12117 G       A       .       .       MORGAN=0.582608;SVTYPE=SNV
(...)

```









