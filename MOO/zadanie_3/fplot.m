function fplot (fn,x1,y1)

fn = strrep(fn,'*','.*');
fn = strrep(fn,'/','./');
fn = strrep(fn,'^','.^');


fn
f=inline(fn, 'x', 'y');

X=-3:0.25:3;
Y=X;
%	nx = 46;
%	ny = 56;
%	X = linspace (-1.5, 1.5, nx);
%	Y = linspace (-0.5, 1.5, ny);


%[x1 y1] = meshgrid(x1,y1);

[X Y]=meshgrid(X,Y);


Z=f(X,Y);
zz=f(x1,y1);

contour(X,Y,Z)
line(x1,y1,zz)