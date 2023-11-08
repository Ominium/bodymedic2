package medic.bodymedic.dto;

import lombok.Data;

@Data
public class PageDTO {
    private int num, start, finish = 0;
    private int pageNum_cnt = 10;
    private int endPageNum = 0;
    // 표시되는 페이지 번호 중 첫번째 번호
    private int startPageNum = 0;
    private int endPageNum_tmp;
    private boolean prev;
    private boolean next;
    public PageDTO(int a,int count){
        num = a;
        start = (num -1) * 10 ;
        endPageNum = (int)(Math.ceil((double)num / (double)pageNum_cnt)) * pageNum_cnt;
        startPageNum = endPageNum - (pageNum_cnt - 1);
        endPageNum_tmp = endpage(count);
        if(endPageNum > endPageNum_tmp) {
            endPageNum = endPageNum_tmp;
        }
        if(start <= 1) start = 0;
        prev = num != 1;
        next = num * pageNum_cnt < count;
    }
    int endpage(int count){
        return (int)(Math.ceil((double)count / (double)pageNum_cnt));
    }

}
