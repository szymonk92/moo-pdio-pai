

function [c,yc,iter] = fibonaccio (f,a,b,eps)
%Input : 
%   f badana funkcja
%   a poczatek przedzialu
%   b koniec przedzialu
%   eps maksymalny oczekiwany b³ad rozwiazania
%Output :
%   c,yc as cy=f(c) rozwiazanie
%jeœli nie ma rozwiazania, [-1,-1,-1]

%szukamy takiego n aby...


c=-1;yc=-1;iter=-1;
outX=[];
outY=[];
aA=a;
bB=b;
f=inline(f);

%fplot(f,[a b]);

[a,b]=unimodality_check(f,a,b,1);
if a ~= b


n=0;
while 1.0/fib(n) > eps
    n=n+1;
end
n
i=0;

x1=b-(fib(n-1)/fib(n))*(b-a);
x2=a+(fib(n-1)/fib(n))*(b-a);


    %szukamy tak d³ugo a¿ nie osi¹gniemy oczekiwanej dok³adnoœci 
    while ( abs(x2 - x1) > eps && n >= 2)
        c=(a+b)/2;
        yc=feval(f,c);
        outX=horzcat(outX,[c]);
        outY=horzcat(outY,[yc]);
        if feval(f,x1)<feval(f,x2)
            b=x2;
            x2=x1;
            n=n-1;
            x1=b-(fib(n-1)/fib(n))*(b-a);
        else
            a=x1;
            x1=x2;
            n=n-1;
           x2=a+(fib(n-1)/fib(n))*(b-a);
        end
        i=i+1;
    end
   iter=i;
   c=(a+b)/2;
   yc=feval(f,c);
   
   

plotfp(f,outX,outY,aA,bB)

end

   
function x = fib(n)
if n<=0
    x=0;
elseif n==1
    x=1;
else x=fib(n-1)+fib(n-2);
end


%[c,yc,iter] = fibonaccio ('x^2 -4',-1,3,0.001)