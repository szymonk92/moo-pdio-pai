function  plotfp(f,x,y,a,b)
%Input
%   f funkcja do narysowania
%   a,b przedzial wartosci
%   x,y wektory puktow
%Output
%   wykres :-)
x
y
xf=a:0.01:b;
yf=zeros(size(xf));

[r,c]=size(xf);

for i=1:c
yf(i)=feval(f,xf(i));
end


plot(xf,yf,'-',x,y,'*')