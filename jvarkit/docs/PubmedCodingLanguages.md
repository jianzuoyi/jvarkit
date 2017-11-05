# PubmedCodingLanguages

Programming language use distribution from recent programs / articles


## Usage

```
Usage: pubmedcodinglang [options] Files
  Options:
    -h, --help
      print help and exit
    --helpFormat
      What kind of help
      Possible Values: [usage, markdown, xml]
    -o, --output
      Output file. Optional . Default: stdout
    --version
      print version and exit

```


## Keywords

 * pubmed
 * xml
 * code
 * programming



## See also in Biostars

 * [https://www.biostars.org/p/251002](https://www.biostars.org/p/251002)


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
$ make pubmedcodinglang
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

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/pubmed/PubmedCodingLanguages.java
](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/pubmed/PubmedCodingLanguages.java
)
## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **pubmedcodinglang** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)


## Example

```
$ java -jar dist/pubmeddump.jar 'Bioinformatics 2017' | java -jar dist/pubmedcodinglang.jar

(...)
28464826	java	microTaboo: a general and practical solution to the k-disjoint problem.	2017	, running [java] 7 and hig
28453684	python	MAPPI-DAT: data management and analysis for protein-protein interaction data from the high-throughput MAPPIT cell microarray platform.	2017	eloped in [python], using r 
28453672	python	Primerize-2D: automated primer design for RNA multidimensional chemical mapping.	2017	and-alone [python] package f
28449031	python	RNAblueprint: Flexible multiple target nucleic acid sequence design.	2017	ritten in [python] to demons
28444126	ruby	CImbinator: A web-based tool for drug synergy analysis in small- and large-scale datasets.	2017	ritten in [ruby] and r. it
28437135	python	D3GB: An Interactive Genome Browser for R, Python, and WordPress.	2017	ackage, a [python] module, a
28431529	python	HirBin: high-resolution identification of differentially abundant functions in metagenomes.	2017	nted as a [python] package a
28430977	java	Deep Mining Heterogeneous Networks of Biomedical Linked Data to Predict Novel Drug-Target Associations.	2017	eloped in [java] and it is
28415074	python	NaviCom: a web application to create interactive molecular network portraits using multi-level omics data.	2017	avicom, a [python] package a

```

Plotting:

```
cut -f2,4 output.txt  | sort | uniq -c |\
awk '{printf("%s\t%s\t%s\n",$3,$2,$1);}' | ./yxv2table -n 0 | grep -v '^null' | grep -v '^     ' | sed 's/^#/Year/' > table.txt && \
gnuplot script.gnuplot
``` 



```
set terminal png size 2000,1500;
set output "output.png"
set title "Bioinformatics/language"
set key invert reverse Left outside
set key autotitle columnheader
set yrange [0:]
set auto x
unset xtics
set xlabel "Year"
set ylabel "Count"
set xtics nomirror rotate by -45 scale 0
set style data histogram
set style histogram rowstacked
set style fill solid border -1
set boxwidth 0.75
N=`awk 'NR==1 {print NF}' table.txt`
plot 'table.txt' using 2:xtic(1), for [i=3:N] '' using i;
```


![https://pbs.twimg.com/media/C_AZTzFXoAUpiuI.jpg:large](https://pbs.twimg.com/media/C_AZTzFXoAUpiuI.jpg:large)


