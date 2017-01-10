# kafka-spark-streaming-for-geocoded-ips
The web services can provide geolocation for the ips that are sent to them. For online retail sites, the online customers' ips 
are present in the web logs. After their purchases the online products will be delivered to their homes via suppliers. The suppliers'
locations are crutial for the delivery times, and have a direct impact on the number of deliveries per day and cost.
So selecting the right branches of the suppliers are important. 

## Solution Algorithm
The solution for the above mentioned problem is mainly calculating the distance of the suppliers and the online customers, whose locations 
are very roughly discovered by geocoded ips. For each supplie, the distance to each customer is accumulated. So the supplier
with the least avarage distance becomes the best candidate. If we need to select n suppliers, we select the first n 
suppliers that have the least distance avarage.

Since the operation is associative, we can distribute it to paralell operations, which Spark already does, so our algorithm is suitable for parallel processing.

## Technical Overview
This project covers the architechtural basics of a Spark ecosystem.
Basically it consists of
- Geo Log Generator, that produces live feed for geocoded ips
- Apache Kafka, that is populated by Log Generator and read by Spark Streaming
- Spark Streaming, that persists the RDDs of DStream read from Kafka into hdfs in Parquet columnar format
- Spark Batch Job that does the analysis for the main problem
