#include <stdio.h>
#include <stdlib.h>

void main()
{
 FILE *fp = fopen("/home/peikun/桌面/lab1/open.txt","w");
 fprintf(fp,"write when open the computer.");
 fclose(fp);
}
