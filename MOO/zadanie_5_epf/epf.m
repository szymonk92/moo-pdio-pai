function [ xopt, yopt ] = epf()
%epf( f, h, g, rh, rg, x0, Crh, Crg, kMax )

f='x^4-2*x^2 * y + x^2 + x*y^2 - 2*x  + 4';
h={'x^2  + y^2 - 2'};
g={'0.25*x^2 + 0.75*y^2 - 1'};
rh=5;
rg=5;
kMax=3;
Crh=0.95;
Crg=0.95;
x0=[1;2];




hpenalty='';
gpenalty='';

if ~isempty(h)
    hpenalty=equalConSum(h);
end
if ~isempty(g)
    gpenalty=inEqualConSum(g);
end



xi=x0
fval=subs(f,{'x','y'},{x0(1),x0(2)})

Fp=penaltyFun(f,hpenalty,gpenalty,rh,rg);

%Fp([1 2])

for i=1:kMax
    %
    Fp=penaltyFun(f,hpenalty,gpenalty,rh,rg)
    
    %solve artificial problem with penalty function
    [xi, fval]=fminunc(Fp,xi)
    
    unsatisfied=0;
    for j=1:length(h)
        hc=subs(h(j),{'x','y'},{xi(1),xi(2)});
        if (hc~=0)
            unsatisfied=unsatisfied+1;
        end
    end
    for j=1:length(g)
        gc=subs(char(g(j)),{'x','y'},{xi(1),xi(2)});
        if (gc>0)
            unsatisfied=unsatisfied+1;
        end
    end
    
    %check STOP
    if(unsatisfied==0 || i>=kMax)
        break;
    end

    rh=rh*Crh
    rg=rg*Crg
end


end



