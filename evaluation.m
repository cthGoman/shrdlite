clc
clear m

% clear save  % comment
% save = [];  % comment

f=fopen('javaprolog\evaluation.txt');
c=textscan(f,'%s');
fclose(f);
j=1;
k=1;
for i=1:length(c{1})
    if(all(isstrprop(c{1}{i},'digit')))
        m(k,j)=str2num(c{1}{i});
        j=j+1;
        if(j>4)
            j=1;
            k=k+1;
        end
    end
        
end
m(:,5)=m(:,1)-m(:,2);
m
save=[save;mean(m(:,3)) mean(m(:,4)) mean(m(:,5))]
