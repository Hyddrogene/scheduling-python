i=0
while ( $(ls | grep zip | wc -l) -eq 1)
do
    unzip X.zip
    i=i+1
done
