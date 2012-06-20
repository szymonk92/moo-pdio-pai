function fstr=equalConSum(f)

fstr='( ';
for i=1:length(f)-1
    fstr=strcat(fstr,'',f(i),')^2 + ');
end
fstr=strcat(fstr,'(',f(length(f)),')^2 )');