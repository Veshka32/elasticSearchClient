elasticsearch post: 9200

Run curl -X GET http://localhost:9200/
For me helped to change in elasticsearch.bat from %JAVA% to !JAVA! in line 47

Cluster health:
http://localhost:9200/_cat/health?v

Indicies:
http://localhost:9200/_cat/indices?v