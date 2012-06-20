function fstr=constrainSum(f)

fstr='( ';
for i=1:length(f)-1
    fstr=strcat(fstr,'(max(0,',f(i),'))^2 + ');
end
fstr=strcat(fstr,'(max(0,',f(i),'))^2 )');