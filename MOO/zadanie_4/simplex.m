function [subs, A, z]= simplex(A, subs, mm, k)
% A tablica simpleksowa
% subs zmienne bazowe
% mm - minimalizacja/maksymalizacja
% k - faza
format rat;

fprintf('Tableau\n')
disp(A)
fprintf(' click...\n\n')
pause

[m, n] = size(A);
[mi, col] = BlandRule(A(m,1:n-1));
while ~isempty(mi) && mi < 0 && abs(mi) > eps
    t = A(1:m-k,col);
    
    if all(t <= 0)
        if mm == 0
            z = -inf;
        else
            z = inf;
        end
        fprintf('\nnieograniczone rozwiazanie z= %s\n',z)
        return
    end
    %szukamy wartosci p do wymiany bazy
    c = 1:m;
    a=A(1:m-k,n);
    b=A(1:m-k,col);
    l = c(b > 0);
    [small, row] = min(a(l)./b(l));
    row = l(row);
    
    %szukamy q do wymiany bazy
    if ~isempty(row)
        if abs(small) <= 100*eps && k == 1
            [s,col] = BlandRule(A(m,1:n-1));
        end
        
        fprintf('wymiana bazy %g  -> %g\n',row,col)
        %operacja wymiany bazy
        A(row,:)= A(row,:)/A(row,col);
        subs(row) = col;
        for i = 1:m
            if i ~= row
                A(i,:)= A(i,:)-A(i,col)*A(row,:);
            end
        end
        
        [mi, col] = BlandRule(A(m,1:n-1));
    end
    
    fprintf('Tableau\n')
    disp(A)
    fprintf(' click ...\n')
    pause
end
z = A(m,n);
end
