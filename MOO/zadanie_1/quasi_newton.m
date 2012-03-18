function [b,by,k]=quasi_newton(f,a,b,eps)
%Input : 
%   f badana funkcja
%   a poczatek przedzialu
%   b koniec przedzialu
%   delta tolerancja bledu dla b
%   eps maksymalny oczekiwany b³ad rozwiazania
%   maks iteracji
%Output - b wynik
%         - err b³¹d przy osi¹gniêciu rozwi¹zania
%         - k liczba iteracji
%         - y =f(b)

outX=[];
outY=[];
aA=a;
bB=b;
f=inline(f);
%fplot(f,[a b]);

maxIterations=5+round((log(a-b)-log(eps))/log(2));

[a,b]=unimodality_check(f,a,b,1);

if a ~= b
k=1;
while  k < maxIterations    
    p2=b-diffp(f,b)*(b-a)/(diffp(f,b)-diffp(f,a));    
    err=abs(p2-b);
    a=b;
    b=p2;
    by=feval(f,b);
    outX=horzcat(outX,[b]);
    outY=horzcat(outY,[by]);
    if (err<eps)
        break
    end
   k=k+1; 
end


plotfp(f,outX,outY,aA,bB)
end


function var = diffp (f,p)
var=feval(inline(diff(sym(f))),p);


%[b,by,err,k]=quasi_newton('x^2 -4',-1,3,0.001,0.01)