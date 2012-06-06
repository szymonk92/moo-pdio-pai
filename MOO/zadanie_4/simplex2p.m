
function  simplex2p(type, c, A, rel, b)

format compact;

%sprowadzanie do postaci standardowej
if (strcmp(type, 'min'))
    mm = 0;
else
    mm = 1;
    c = -c;
end
c=c(:)';
b=b(:);
[m, n] = size(A);
n1 = n;
les = 0;
if length(c) < n
    c = [c zeros(1,n-length(c))];
end
for i=1:m
    artificial_var =zeros(m,1);
    artificial_var(i)=1;
    if(rel(i) == '<')
        A = [A artificial_var];
        les = les + 1;
    elseif(rel(i) == '>')
        A = [A -artificial_var];
    end
end
ncol = length(A);

if les == m
    %zadanie ma taka postac, ze wystarczy policzyc sobie sympleksa
    c = [c zeros(1,ncol-length(c))];
    A = [A;c];
    A = [A [b;0]];
    [subs, A, z] = simplex(A, n1+1:ncol, mm, 1);
    fprintf('Koniec fazy 1\n')
else
    %musimy znalezc poczatkowe rozwiazanie dopuszczalne
    A = [A eye(m) b];
    if m > 1
        w = -sum(A(1:m,1:ncol));
    else
        w = -A(1,1:ncol);
    end
    %budujemy kanoniczna tablice sympleksowa dla problemu sztucznego
    c = [c zeros(1,length(A)-length(c))];
    A = [A;c];
    A = [A;[w zeros(1,m) -sum(b)]];
    subs = ncol+1:ncol+m;
    av = subs;
    [subs, A, z] = simplex(A, subs, mm, 2);
    fprintf('Koniec fazy 1\n')
    
    nc = ncol + m + 1;
    x = zeros(nc-1,1);
    x(subs) = A(1:m,nc);
    xa = x(av);
    com = intersect(subs,av);
    if (any(xa) ~= 0)
        fprintf('\nBrak rozwiazan\n')
        return
    else
        if ~isempty(com)
        fprintf('\n Uklad rownani jest redundantny\n')
        end
    end
    A = A(1:m+1,1:nc);
    A =[A(1:m+1,1:ncol) A(1:m+1,nc)];
    [subs, A, z] = simplex(A, subs, mm, 1);
    fprintf('Koniec fazy 2\n')
end
if (z == inf || z == -inf)
    return
end
[m, n] = size(A);
x = zeros(n,1);
x(subs) = A(1:m-1,n);
x = x(1:n1);
if mm == 0
    z = -A(m,n);
else
    z = A(m,n);
end

fprintf('\nRozwiazanie optymalne:\n')
disp(x(1:n1))
fprintf('\n wartosc funkcji celu:\n')
disp(z)

t = find(A(m,1:n-1) == 0);
if length(t) > m-1
    fprintf('Problem ma nieskonczenie wiele rozwiazan');
end

end

%cx
%c=[-3/4, 150, -1/50, 6]
%Ax
%A=[1/4, -60, -1/25, 9; 1/2, -90, -1/50, 3; 0, 1, 1, 0]
% <rel> b
%b=[0;0;1]

% type = 'min';
% c = [-3 4];
% A = [1 1;2 3];
% rel = '<>';
% b = [4; 18];


% type = 'max';
% c = [3 2 1];
% A = [2 -3 2;-1 1 1];
% rel = '<<';
% b = [3;55];

% type = 'min';
% c = [3 4 6 7 1 0 0];
% A = [2 -1 1 6 -5 -1 0;1 1 2 1 2 0 -1];
% rel = '==';
% b = [6;3];


% type = 'min';
% c = [-1 2 -3];
% A = [1 1 1;-1 1 2;0 2 3;0 0 1];
% rel = '===<';
% b = [6 4 10 2];

%  type = 'max';
%  c = [7 6];
%  A = [2 1;1 4];
%  rel = '<<';
%  b = [3 4];


% type = 'min';
% c = [-3/4 150 -1/50 6];
% A = [1/4 -60 -1/25 9; 1/2 -90 -1/50 3; 0 0 1 0];
% rel = '<<<';
% b = [0 0 1];

% simplex2p(type,c,A,rel,b);
