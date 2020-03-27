#include <stdio.h>  
#include <stdlib.h>  
#include <string.h>  
#include <sys/types.h>  
#include <unistd.h>  
#include<netinet/in.h> 
#include<arpa/inet.h> 
#include<sys/socket.h>
   
#undef _POSIX_SOURCE  
#include <sys/capability.h>  
extern int errno;

void whoami(void)
{
	printf("uid=%i  euid=%i  gid=%i\n", getuid(), geteuid(), getgid());
}

void listCaps()
{
	cap_t caps = cap_get_proc();
	ssize_t y = 0;
	printf("The process %d was give capabilities %s\n", (int)getpid(), cap_to_text(caps, &y));
	fflush(0);
	cap_free(caps);
}

int main(int argc, char **argv)
{
	int stat;
	whoami();
	stat = setuid(geteuid());
	pid_t parentPid = getpid();

	if (!parentPid)
		return 1;
	cap_t caps = cap_init();

	cap_value_t capList[5] = { CAP_NET_RAW, CAP_NET_BIND_SERVICE , CAP_SETUID, CAP_SETGID,CAP_SETPCAP };
	unsigned num_caps = 5;
	cap_set_flag(caps, CAP_EFFECTIVE, num_caps, capList, CAP_SET);
	cap_set_flag(caps, CAP_INHERITABLE, num_caps, capList, CAP_SET);
	cap_set_flag(caps, CAP_PERMITTED, num_caps, capList, CAP_SET);

	if (cap_set_proc(caps)) {
		perror("capset()");

		return EXIT_FAILURE;
	}
	listCaps();

        int server_socket = socket(AF_INET, SOCK_STREAM, 0);
        if(server_socket < 0) 
        {
            printf("erro \n");
        }
	// bind 绑定
	struct sockaddr_in server_sockaddr;
	memset(&server_sockaddr, 0, sizeof(server_sockaddr));
	server_sockaddr.sin_family = AF_INET;
	server_sockaddr.sin_port = htons(80);
	server_sockaddr.sin_addr.s_addr = inet_addr("127.0.0.1");
	int is_bind = bind(server_socket, (struct sockaddr *)&server_sockaddr,
		sizeof(server_sockaddr));
	if (is_bind < 0)
	{
		printf("bind error \n");
	}
	else
	{
		printf("bind success \n");//预计绑定成功
	}

	printf("dropping caps\n");
	cap_clear(caps);  // resetting caps storage  

	if (cap_set_proc(caps)) {
		perror("capset()");
		return EXIT_FAILURE;
	}
	listCaps();

	is_bind = bind(server_socket, (struct sockaddr *)&server_sockaddr,
		sizeof(server_sockaddr));//尝试绑定
	if (is_bind < 0)
	{
		printf("bind error \n");//预计绑定失败
	}
	else
	{
		printf("bind success \n");
	}
	cap_free(caps);
	return 0;
}
