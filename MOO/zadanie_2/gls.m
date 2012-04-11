function x=gls(x0,sl,fn,a0,rho,c,kMax)
% Metoda najszybszego spadku
% INPUT:
%       x0 - punkt pocz¹tkowy;
%       sl - nazwa kryterium;
%       f0 - funkcja podstawowa napis(np 'X^2+Y^2')   
%       a0 - pocz¹tkowa d³ugoœc kroku;
%       rho - parametr backtrackingu
%       c - sta³a wykorzystywana w kryteriach
% OUTPUT:
%       x - rozwi¹zania;



%ugotuj funkcje podstawow¹ z stringa
f=inline(fn);
f0=@(x)f(x(1),x(2));

%ugotuj gradien na podstawie przepisu w stringu
syms x y
t=[x;y];
w=fn;
gfn=matlabFunction(jacobian(w,t).');
f1=@(x)gfn(x(1),x(2));

%wybor kryterium
sl=@(x,p)backtk(sl,a0,x,p,c,f0,f1,rho);
x=x0;
k=0;

while k<kMax %warunek zakoñczenia, liczba iteracji
    %kierunek
    p=-f1(x);
    %d³ugoœæ kroku spe³niaj¹ca kryterium
    a=sl(x,p);
    %kolejne przybli¿enie
    x=x+a*p
    %licznik iteracji
    k=k+1;
end
%Przyk³ady
%gls([2;2],'armijo','X^2+Y^2',0.36,0.5,0.2,10)
%gls([2;2],'wolfes','X^2+Y^2',0.35,0.5,[0.8;0.5],10)
%gls([2;2],'wolfes','X^2+Y^2',0.35,0.5,[0.8;0.5],10)
%gls([2;2],'goldstein','X^2+Y^2',0.35,0.5,0.2,10)
     
     