#include <stdio.h>
int main(void){
float x;
float y;
float result;
x = 5;
y = 10;
result = 0;
if(x<y){
printf("x is less than y\n");
}
while(x<y){
x = x+1;
result = result+x;
printf("%.2f\n", (float)(result));
}
end:
goto end;
printf("This will never print\n");
return 0;
}
