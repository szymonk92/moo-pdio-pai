function [c,yc,iter] = bisection (f,a,b,eps)
%Input : 
%   f badana funkcja
%   a poczatek przedzialu
%   b koniec przedzialu
%   eps maksymalny oczekiwany b³ad rozwiazania
%Output :
%   c,yc as cy=f(c) rozwiazanie
%jeœli nie ma rozwiazania, [-1,-1,-1]

outX=[];
outY=[];
aA=a;
bB=b;
f=inline(f);
%fplot(f,[a b]);
c=-1;yc=-1;iter=-1;
[a,b]=unimodality_check(f,a,b,1);

xL=a;xR=b;


maxIterations=5+round((log(a-b)-log(eps))/log(2));
fprintf('max iterations %d\n',maxIterations);
i=0;

if  a ~= b

    %szukamy tak d³ugo a¿ nie osi¹gniemy oczekiwanej dok³adnoœci b¹dŸ nie
    %osi¹gniemy maksymalnego górnego ograniczenia dla oczekiwanej iloœci
    %iteracji
    while ( abs(xR - xL) > eps && i< maxIterations)
        c=(xL+xR)/2.0;
        yc=feval(f,c);
        outX=horzcat(outX,[c]);
        outY=horzcat(outY,[yc]);
        l=xR-xL;
        x1=xL+(l/4.0);
        x2=xR-(l/4.0);
        if feval(f,x1)<feval(f,c)
            xR=c;
            c=x1;
        elseif feval(f,x2)<feval(f,c)
            xL=c;
            c=x2;
        else
            xL=x1;
            xR=x2;
        end
        i=i+1;
    end
    iter=i

    plotfp(f,outX,outY,aA,bB)
end

%plot(outX,outY,'.')

%[c,yc,iter] = bisection  ('x^2 -4',-1,3,0.001)