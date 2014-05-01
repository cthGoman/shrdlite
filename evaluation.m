clc
clear all
f=fopen('javaprolog\evaluation.txt');
c=textscan(f,'%s');
fclose(f);
j=1;
k=1;
for i=1:length(c{1})
    if(all(isstrprop(c{1}{i},'digit')))
        m(k,j)=str2num(c{1}{i});
        j=j+1;
        if(j>3)
            j=1;
            k=k+1;
        end
    end
        
end
m