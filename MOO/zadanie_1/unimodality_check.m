function [a, b] = unimodality_check(f,a,b, step)

%f=inline(f);
unimodal=0;
a=a+step;

while unimodal==0 && a<b
    %found max || min
    if (feval(f,a-step) < feval(f,a) && feval(f,a+step) < feval(f,a)) || (feval(f,a-step) > feval(f,a) && feval(f,a+step) > feval(f,a))
        unimodal=1;
    else
        a=a+step;
    end
end

if unimodal == 0
    disp('przy danym kroku probkowania, nie znaleziono podprzedzialu unimodalnosci');
    a=-1;b=-1; 
elseif unimodal == 1
    b=a+step;
end