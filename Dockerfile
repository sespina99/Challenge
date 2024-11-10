FROM ubuntu:latest
LABEL authors="segundo"

ENTRYPOINT ["top", "-b"]