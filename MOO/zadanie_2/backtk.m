function a=backtk(cond,a0,x,p,c,f0,f1,rho)
% Algorytm backtrackingu - poszukiwanie d³ugoœci kroku spe³niaj¹cej
% kryterium
% INPUT:
%       cond - nazwa kryterium
%       a0 - pocz¹tkowa d³ugoœæ kroku
%       x - aktualne przybli¿enie
%       p - kierunek poszukiwania
%       c - sta³a u¿ywana w kryterium
%       f0-funkcja podstawowa f(Rn)->R
%       f1-gradient funkcji podstawowej f(Rn)->Rn
%       rho - mno¿nik u¿ywany w algorytmie (0,1)
% OUTPUT:
%       a - d³ugoœæ kroku dla której zachodzi kryterium
switch cond
    case 'armijo'
        check=@(a)armijo(a,x,p,c,f0,f1);
    case 'goldstein'
        check=@(a)goldstein(a,x,p,c,f0,f1);
    case 'wolfe'
        check=@(a)wolfe(a,x,p,c,f1);
    case 'wolfes'
        check=@(a)wolfes(a,x,p,c,f1);
end
a=a0;
while check(a)==0  %poszukiwanie d³ugoœci kroku dla której
                   %zachodziæ bêdzie dane kryterium
    a=rho*a;
end
    