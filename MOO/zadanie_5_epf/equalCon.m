function [ hconstrains ] = equalCon( h )

for i=1:length(h)
    hconstrains(i)=strcat('(',h,')==0');
end


end

