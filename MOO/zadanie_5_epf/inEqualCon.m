function [ gconstrains ] = inEqual( g )


for i=1:length(g)
    gconstrains(i)=strcat('(',g,')<=0');
end

end

