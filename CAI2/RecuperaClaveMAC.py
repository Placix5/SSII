import hmac
import struct
from hashlib import sha1
from mpi4py import MPI
import time
#Para el primero
comm= MPI.COMM_WORLD
rank=comm.Get_rank()
size= comm.Get_size()

def hmac_sha1(key, msg):
    return hmac.HMAC(key,msg,sha1)
msg="541158 487656 200"#Mensaje mandado
datoparcial=["","","","","","","","","","","","","","",""]
def iteraciones(i,f):
    for a in range(i,f):
        datoparcial[rank]= bytes([a])
        for b in range(0,256):
            datoparcial[rank+1] = datoparcial[rank] + bytes([b])
            for c in range(0,256):
                datoparcial[rank+2] = datoparcial[rank+1] + bytes([c])
                for d in range(0,256):
                    datoparcial[rank+3] = datoparcial[rank+2] + bytes([d])
                    h=hmac_sha1(datoparcial[rank+3],msg.encode('utf-8'))
                    if h.hexdigest() == "0a5f910eddc60e3b06f51670e83d37886804bf9a":#MAC con la que compararemos
                        print("Hola soy "+str(rank)+" y este es el resultado: "+ str(datoparcial[rank+3]))
                        print ("--Lo que tarda en encontrar la solucion---", int((time.time()-start_time)), "segundos-----")
                        break

start_time = time.time()
iteraciones(int((256/4)*rank),int((256/4)*(rank+1)))
comm.Barrier()
if rank==0:
    print ("---Lo que tarda en ejecutarse el programa completo--", int((time.time()-start_time)), "segundos-----")
    print("Este es el resultado: "+ str(datoparcial[rank+2]))