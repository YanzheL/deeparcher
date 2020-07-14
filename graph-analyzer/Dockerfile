FROM rapidsai/rapidsai:cuda10.2-base-ubuntu18.04-py3.7

WORKDIR /app

COPY environment.yml ./

RUN conda env update -n rapids -f environment.yml && \
    conda clean --all -y

COPY . .

CMD ["python", "main.py"]
