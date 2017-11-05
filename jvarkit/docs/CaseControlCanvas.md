# Main


## Usage

```
Usage: Main [options] Files
  Options:
    --paper, --background
      background color.  A named color ('red', 'blue'...) use the syntax 
      'rgb(int,int,int)'. 
      Default: java.awt.Color[r=255,g=0,b=0]
    -caseAtt, --caseAttribute
      Do not calculate MAF for cases, but use this tag to get Controls' MAF. 
      Notation 'AC/AN' will use two attributes
    -ctrlAtt, --ctrlAttribute
      Do not calculate MAF for controls, but use this tag to get Cases' MAF. 
      Notation 'AC/AN' will use two attributes
    --pen, --foreground
      pen color.  A named color ('red', 'blue'...) use the syntax 
      'rgb(int,int,int)'. 
      Default: java.awt.Color[r=255,g=200,b=0]
    -format, --format
      How to print doubles, printf-like precision format.
      Default: %.5f
    -h, --help
      print help and exit
    -nchr, --nocallhomref
      Consider no call as hom-ref
      Default: false
    -opacity, --opacity
      opacity [0-1]
      Default: 0.6
    -o, --out
      Image file name. Output file. Optional . Default: stdout
    -ped, --pedigree
      A pedigree is a text file delimited with tabs. No header. Columns are 
      (1) Family (2) Individual-ID (3) Father Id or '0' (4) Mother Id or '0' 
      (5) Sex : 1 male/2 female / 0 unknown (6) Status : 0 unaffected, 1 
      affected,-9 unknown  If not defined, I will try to extract the pedigree 
      from the VCFheader.
    --pointshape
      Point Shape
      Default: oval
      Possible Values: [oval, square, cross]
    --pointsize
      points width
      Default: 10.0
    -sexchr, --sexualchromosomes
      comma separated list of chromosomes that should be considered as sexual 
      chromosomes/haploids 
      Default: chrX,chrY,X,Y
    -tee, --tee
      Tee input to stdout, useful in linux pipelines to get intermediary 
      results. Requires that -o 'file' is set.
      Default: false
    -txt, --txt, --text, -tsv, --tsv
      Input is a tab delimited file. containg x=case and y=controls
      Default: false
    -title, --title
      Title
      Default: <empty string>
    --version
      print version and exit
    --width
      Canvas width
      Default: 700
    -xyAttribute, --xyAttribute
      When using 'tee', add this Attribute containing the MAF for case and 
      control 
      Default: MAFCASECTRL

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
$ make software
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

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/burden/CaseControlCanvas$Main.java
](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/burden/CaseControlCanvas$Main.java
)
## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **software** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)


## Example

Create Exac/Gnomad from a VCF annotated with both databases:

```
java -jar casectrlcanvas.jar -caseAtt AC_NFE/AN_NFE -ctrlAtt gnomad_exome_AF_NFE -o exome_gnomad_vs_exac.png.NFE.png -title 'Case:ExacAC/AN_NFE Ctrl:gnomad_exome_AF_NFE' -opacity 0.2 input.vcf
```


Create Exac/my-controls from a VCF annotated with both databases:

```
java -jar casectrlcanvas.jar -p myped.ped -caseAtt AC_NFE/AN_NFE   -o out.png -opacity 0.2 input.vcf
```

## Note to self: create a mosaic of images:

```
montage -geometry 1000x1000+2+2 file1.png file2.png fileN.png out.png
```



