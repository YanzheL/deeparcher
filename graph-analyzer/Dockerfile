FROM rapidsai/rapidsai:0.16-cuda11.0-base-ubuntu18.04-py3.8

WORKDIR /app

COPY environment.yml ./

RUN conda env update -n rapids -f environment.yml && \
    conda clean --all -y

COPY . .

CMD ["python", "main.py"]
