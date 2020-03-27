#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>

int main() 
{
    int ruid, euid, suid;
    getresuid(&ruid, &euid, &suid);
    printf("pre:\nruid:%d\teuid:%d\tsuid:%d\n", ruid, euid, suid);
    chroot("/home/rocketeerli/myftp");
    setresuid(1001, 1001, 1001);
    getresuid(&ruid, &euid, &suid);
    printf("post:\nruid:%d\teuid:%d\tsuid:%d\n", ruid, euid, suid);
}
