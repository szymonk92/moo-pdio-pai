#!/bin/bash

#convert -size 100x100 xc: +noise Random noise.png
#convert noise.png -virtual-pixel tile -blur 0x1 -fx intensity -normalize noise-mask.png

#listowanie plikow b-[0-9]+ =find dir/ -regex  ".*\/b-[0-9]+\.png"


files=( $1 )
#echo ${files[*]}

for i in ${files[*]}
do
echo $i
	convert ${i} -gravity center -crop 94x94+0+0\! -background white -flatten -attenuate 0.8 +noise Gaussian -adaptive-blur 2x  -resize 30x30 `echo $i | cut -d'.' -f1`-ozakaz-noise0.png
	
done