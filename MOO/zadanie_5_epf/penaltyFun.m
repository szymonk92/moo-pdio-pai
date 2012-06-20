function fout = penaltyFun( f, h, g, rh, rg )

hfun=0;
gfun=0;

if ~isempty(h)
    hfun=@(x,y)subs(h,{'x','y'},{x,y});
end
if ~isempty(g)
    gfun=@(x,y)subs(g,{'x','y'},{x,y});
end

ffun=@(x,y)subs(f,{'x','y'},{x,y});

fout = @(x) ffun(x(1),x(2)) + rh*hfun(x(1),x(2)) + rg*gfun(x(1),x(2));

end

