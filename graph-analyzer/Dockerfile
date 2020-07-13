FROM rapidsai/rapidsai:cuda10.2-base-ubuntu18.04-py3.7

WORKDIR /app

COPY environment.yml ./

RUN conda env create -f environment.yml -p ./envs

COPY . .

CMD ["conda", "run", "-p", "./envs", "python", "main.py"]
