#!/bin/bash

# -unsharp 0.5 \( +clone -negate \) -edge 1  -compose add -composite  -negate



if [ $# -lt 2 ];
then
	echo "zle"
	exit
fi

input=$1
output=$2

for i in `ls $input/*.png`
do
filename=`echo $i | awk 'BEGIN { FS="/"};{print $NF}'`
#.././redist.sh -s uniform $i "../output_adjust/${i}"
convert -normalize -auto-level -resize 30x30 -unsharp 0x1 $i  "${output}${filename}"
done




