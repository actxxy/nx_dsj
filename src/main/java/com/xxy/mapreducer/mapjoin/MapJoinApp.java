package com.xxy.mapreducer.mapjoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class MapJoinApp {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        job.setJarByClass(MapJoinApp.class);


        job.setMapperClass(joinMapper.class);
        job.setMapOutputKeyClass(MovieRate.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setNumReduceTasks(0);
        // 通过job对象,指定将会缓存到各个将要执行maptask的服务器节点上的缓存文件
        URI uri = new URI("/tmp/mapjoin/movies.dat");
        job.addCacheFile(uri);

        Path maxPath = new Path("/tmp/mapjoin/rate");
        Path outPath = new Path("/tmp/mapjoin/result");
        FileSystem fs = FileSystem.get(configuration);
        if (fs.exists(outPath)) {
            fs.delete(outPath, true);
        }
        FileOutputFormat.setOutputPath(job,outPath);
        FileInputFormat.setInputPaths(job, maxPath);
    }

    static class joinMapper extends Mapper<LongWritable, Text, MovieRate, NullWritable> {
        // 存放mapjoin的小数据集
        private Map<String,String> movieMap =new HashMap<String, String>();
        // 输出对象,提前初始化
        MovieRate outkey = new MovieRate();

        @Override
        protected void setup(Mapper<LongWritable, Text, MovieRate, NullWritable>.Context context) throws IOException, InterruptedException {
            Path[] localCacheFiles = context.getLocalCacheFiles();
            URI[] cacheFiles = context.getCacheFiles();
            String strPath = localCacheFiles[0].toUri().toString();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(strPath));
            String readline = null;

            while (null != (readline = bufferedReader.readLine())) {
                System.out.println(readline);
                String[] split = readline.split("::");
                String moveid = split[0];
                String moviename = split[1];
                String movieType = split[2];
                movieMap.put(moveid, moviename + "::" + movieType);
            }
            IOUtils.closeStream(bufferedReader);
        }

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, MovieRate, NullWritable>.Context context) throws IOException, InterruptedException {
            String[] splits = value.toString().split("::");
            String movieid = splits[1];
            int rate = Integer.parseInt(splits[2]);
            long ts = Long.parseLong(splits[3]);
            String userid = splits[0];

            // map根据key到 movieMap 中匹配小表数据
            String movieNameAndType = movieMap.get(movieid);
            String movieName = movieNameAndType.split("::")[0];
            String movieType = movieNameAndType.split("::")[1];
            MovieRate movieRate = new MovieRate(movieid, userid, rate, movieName, ts);
            context.write(movieRate, NullWritable.get());
        }
    }
}
