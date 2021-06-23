import socket
import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import CountVectorizer  # 벡터화
from sklearn.metrics.pairwise import cosine_similarity  # 코사인 유사도
from numpy import dot
from numpy.linalg import norm
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel


# 접속할 서버 주소입니다. 여기에서는 루프백(loopback) 인터페이스 주소 즉 localhost를 사용합니다. 
HOST = ""

# 클라이언트 접속을 대기하는 포트 번호입니다.   
PORT = 9999        

df = pd.read_csv('user_data.csv', low_memory=False)

df['mix'] = df['animal'] + " " + df['time'] + " " + df['length'] + " " + df['place'] + " " + df['purpose']

tfidf = TfidfVectorizer(stop_words='english')

tfidf_matrix = tfidf.fit_transform(df['mix'])

cosine_sim = linear_kernel(tfidf_matrix, tfidf_matrix)
indices = pd.Series(df.index, index=df['user']).drop_duplicates()

def get_recommendations(animal, time, length, place, purpose, cosine_sim=cosine_sim):

    if animal == '0':
        animal = "yes"
    else:
        animal = "no"
        
    if time == '0':
        time = "day"
    else:
        time = "night"
        
    if length == '0':
        length = "short"
    else:
        length = "long"
        
    if place == '0':
        place = "forest"
    else:
        place = "city"
        
    if purpose == '0':
        purpose = "walk"
    else:
        purpose = "exercise"
        
    result = animal + " " + time + " " + length + " " + place + " " + purpose

    print(result)
    
    a = df['mix']
    
    for i in range(32):
        if a[i] == result:
            num = i+1
            
    result = "user_%d"%(num)

    idx = indices[result]
    
    sim_scores = list(enumerate(cosine_sim[idx]))
    
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
    
    sim_scores = sim_scores[0:5]

    movie_indices = [i[0] for i in sim_scores]

    result = df['user'].iloc[movie_indices].array

    user1 = result[0]
    user2 = result[1]
    user3 = result[2]
    user4 = result[3]
    user5 = result[4]

    users = user1 + ',' + user2 + ',' + user3 + ',' + user4 + ','  + user5

    return users

# 소켓 객체를 생성합니다. 
# 주소 체계(address family)로 IPv4, 소켓 타입으로 TCP 사용합니다.  
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)


# 포트 사용중이라 연결할 수 없다는 
# WinError 10048 에러 해결를 위해 필요합니다. 
server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)


# bind 함수는 소켓을 특정 네트워크 인터페이스와 포트 번호에 연결하는데 사용됩니다.
# HOST는 hostname, ip address, 빈 문자열 ""이 될 수 있습니다.
# 빈 문자열이면 모든 네트워크 인터페이스로부터의 접속을 허용합니다. 
# PORT는 1-65535 사이의 숫자를 사용할 수 있습니다.  
server_socket.bind((HOST, PORT))

# 서버가 클라이언트의 접속을 허용하도록 합니다. 
server_socket.listen()

# accept 함수에서 대기하다가 클라이언트가 접속하면 새로운 소켓을 리턴합니다. 
client_socket, addr = server_socket.accept()

# 접속한 클라이언트의 주소입니다.
print('Connected by', addr)

# 무한루프를 돌면서 
while True:

    # 클라이언트가 보낸 메시지를 수신하기 위해 대기합니다. 
    data = client_socket.recv(1024)

    # 빈 문자열을 수신하면 루프를 중지합니다. 
    if not data:
        break

    # 수신받은 문자열을 출력합니다.
    print('Received from', addr, data.decode('CP949'))

    #사용자 선호도 함수 돌리기

    de_data = data.decode('CP949')
    pref = de_data.split(',')
    pref[0] = pref[0][-1:]
    print(pref[0])
    print('decoded', pref)
    user = get_recommendations(pref[0],pref[1],pref[2],pref[3],pref[4])
    print(user)

    # 받은 문자열을 다시 클라이언트로 전송해줍니다.(에코)
    client_socket.sendall(user.encode())
    print('Send Success')


# 소켓을 닫습니다.
client_socket.close()
server_socket.close()
