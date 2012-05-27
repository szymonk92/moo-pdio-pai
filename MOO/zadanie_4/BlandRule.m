%regula Blanda
function [m, j] = BlandRule(d)
m = [];
j = [];
ind = find(d < 0);
if ~isempty(ind)
    j = ind(1);
    m = d(j);
end
end