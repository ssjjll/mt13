#!/usr/bin/python3

import re, os, sys

def readvcb(vcbfn):
    vcb = {}
    with open(vcbfn, 'r', encoding='utf8') as f:
        for line in f.readlines():
            line=line.strip()
            words = re.split(r'\s+', line)
            ind = int(words[0])
            w = words[1]
            freq = int(words[2])

            vcb[ind] = w
    return vcb

def decode(line, vcb):
    ret = []

    for s in line.split(" "):
        if len(s) == 0: continue
        ret.append(vcb[int(s)])
    return " ".join(ret)


if __name__ == "__main__":
"""
read the original sentence pair in snt in case sentences are truncated
read A3.final generated by readA3.py, zh-en and en-zh
read thg original vocabulary file for encoding/decoding
output to stdout a merged file includes 5-line records:
#lineno##
source sentence
target sentence
source to target wa array
target to source wa array
NOTE: Some word alignments are truncated to 100
"""


    #f1 = sys.argv[1]
    #f2 = sys.argv[2]
    #f3 = sys.argv[3]
    #f4 = sys.argv[4]
    #f5 = sys.argv[5]

    f1 = "/home2/jlsong/mt13/data50k-zh-en/train50k.zh_train50k.en.snt"
    f2 = "/home2/jlsong/mt13/data50k-zh-en/113-11-29.023846.admin.A3.final"
    f3 = "/home2/jlsong/mt13/data50k-en-zh/113-12-03.004406.admin.A3.final"
    f4 = "/home2/jlsong/mt13/data50k-zh-en/train50k.zh.vcb"
    f5 = "/home2/jlsong/mt13/data50k-zh-en/train50k.en.vcb"

    vcbs = readvcb(f4)
    vcbt = readvcb(f5)

    dumpString=True

    fsnt= open(f1, "r", encoding="utf8")

    fswa = open(f2, "r", encoding="utf8")
    
    ftwa = open(f3, "r", encoding="utf8")


    while True:
        l1n = fswa.readline()
        if len(l1n)==0:
            break
        l1n = l1n.strip()

        l1e = fswa.readline().strip()
        l1z = fswa.readline().strip()
        l1wa = fswa.readline().strip()

        l2n = ftwa.readline().strip()
        l2z = ftwa.readline().strip()
        l2e = ftwa.readline().strip()
        l2wa = ftwa.readline().strip()

        l31 = fsnt.readline().strip()
        l3z_enc = fsnt.readline().strip()
        l3z = decode(l3z_enc, vcbs)

        l3e_enc = fsnt.readline().strip()
        l3e = decode(l3e_enc, vcbt)

        #if l1z == l2z and l1z == l3z and l1e == l2e and l1e == l3e:
            #continue
        
        print(l1n)
        if (dumpString):
            print(l3z)
            print(l3e)
        else:
            print(l3z_enc)
            print(l3e_enc)
        print(l2wa)
        print(l1wa)
