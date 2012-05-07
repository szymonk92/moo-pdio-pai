function test=goldstein(a,x,p,c,f0,f1)
% Funkcja sprawdza czy dla dla danej d³ugoœci kroku zachodzi kryterium
% goldsteina
% 
% INPUT:
%       a-aktualna d³ugoœc kroku [n]
%       x-aktualne przybli¿enie [n;...]
%       p-kierunek poszukiwania [n;...]
%       c-sta³a wykorzystywana w kryterium (0,1), 
%       powinna byæ mniejsza od tej u¿ytej w armijo
%       f0-funkcja podstawowa f(Rn)->R
%       f1-gradient funkcji podstawowej f(Rn)->Rn
% OUTPUT:
%       test - prawda/fa³sz, czy warunek zachodzi

if f0(x+a*p)<=f0(x)+c*a*f1(x)'*p && f0(x+a*p)>=f0(x)+(1.0-c)*a*f1(x)'*p
    test=1;
else 
    test=0;
end
end