package com.nx.disruptor.performance;

import com.lmax.disruptor.EventHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DataEventHandler implements EventHandler<DataEvent> {

    private long startTime;
    private int i;

    public DataEventHandler() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onEvent(DataEvent dataEvent, long l, boolean b) throws Exception {
        log.info("消费数据");
        i ++ ;
        if(i >= Constants.EVENT_NUM_BAI - 1){
            long endTime = System.currentTimeMillis();
            log.info("Disruptor costTime = " + (endTime - startTime) + "ms");
        }
    }
}
