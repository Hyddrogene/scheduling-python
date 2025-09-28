version: '3'

services:
  php-service:
    image: php:8.2-cli
    volumes:
      - /home/etud/timetabling/tools/parser_tools:/app/parser_tools
      - /home/etud/timetabling/tools/tools_php:/app/tools_php
    working_dir: /app/tools_php
