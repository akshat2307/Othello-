import java.io.*;
import java.util.*;

public class Othello {
    int turn;
    int winner;
    int board[][];
    private int[][] direcs;


    public Othello(String filename) throws Exception {
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        turn = sc.nextInt();
        board = new int[8][8];
        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j){
                board[i][j] = sc.nextInt();
            }
        }
        winner = -1;
        direcs = new int[8][2];
        int curx = 0;
        for(int i = -1; i<2; i++){
            for(int j = -1; j<2; j++){
                if(i==0 && j==0)continue;
                else{
                    direcs[curx][0]=i;
                    direcs[curx][1]=j;
                    curx++;
                }
            }
        }
    }



    public int boardScore() {
        int bp = 0;
        int wp = 0;
        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j){
                if(board[i][j]==0)bp++;
                else if(board[i][j]==1)wp++;
            }
        }
        if(turn==0)return bp-wp;
        return wp-bp;
    }

    private int bminusw(int[][] brd){
        int bp = 0;
        int wp = 0;
        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j){
                if(brd[i][j]==0)bp++;
                else if(brd[i][j]==1)wp++;
            }
        }
        return bp-wp;
      
    }

    public int bestMove(int k) {
        int dep = k;
        int ans = minimax(board, turn, dep, false)[1];
        return ans;
    }

    private int[] minimax(int[][] curboard, int t, int k, Boolean pp){
        if(k==0){
            int[] a = new int[2];
            a[0]=bminusw(curboard);
            a[1]=-1;
            return a;
        }

        if(t==0){
            Boolean one = false;
            int[] a = new int[2];
            a[0]=-100;
            a[1]=-1;
            int sc = -100;
            for(int i = 0; i<8; i++){
                for(int j = 0; j<8; j++){
                    if(curboard[i][j]==-1){
                        Boolean valid = false;
                        int cp[][] = new int[8][8];
                        for(int xx = 0; xx < 8; ++xx)
                            System.arraycopy(curboard[xx], 0, cp[xx], 0, 8);
                        for(int d = 0; d<8; d++){
                            int x = i+direcs[d][0];
                            int y = j+direcs[d][1];
                            if(i+direcs[d][0]<0 || i+direcs[d][0]>=8 || j+direcs[d][1]<0 || j+direcs[d][1]>=8){
                                continue;
                            }
                            if(curboard[x][y] == 1){
                                while(!(x+direcs[d][0]<0 || x+direcs[d][0]>=8 || y+direcs[d][1]<0 || y+direcs[d][1]>=8) && curboard[x][y]==1){
                                    x+=direcs[d][0];
                                    y+=direcs[d][1];
                                }
                                if(curboard[x][y]==0){
                                    one = true;
                                    valid = true;
                                    x = i+direcs[d][0];
                                    y = j+direcs[d][1];
                                    while(!(x+direcs[d][0]<0 || x+direcs[d][0]>=8 || y+direcs[d][1]<0 || y+direcs[d][1]>=8) && curboard[x][y]==1){
                                        cp[x][y]=0;
                                        x+=direcs[d][0];
                                        y+=direcs[d][1];
                                    }
                                }
                            }
                        }
                        if(valid){
                            cp[i][j] = 0;
                                                     
                            int[] score_move = minimax(cp, 1, k-1, false);
                            if(score_move[0] > a[0]){
                                
                                a[0]=score_move[0];
                                a[1]=8*i+j;
                            }
                        }
                    }
                }
            }
            if(!one){
                if(pp==true){
                    a[0] = bminusw(curboard);
                    a[1] = -1;
                    return a;
                }
                else{
                    int[] ss = minimax(curboard, 1, k-1, true);
                    a[0] = ss[0];
                    a[1] = -1;
                }
            }
            return a;
        }
        else{
            int[] a = new int[2];
            Boolean one = false;
            a[0]=100;
            a[1]=-1;
            for(int i = 0; i<8; i++){
                for(int j = 0; j<8; j++){
                    if(curboard[i][j]==-1){
                        boolean valid = false;
                        int cp[][] = new int[8][8];
                        for(int xx = 0; xx < 8; ++xx)
                            System.arraycopy(curboard[xx], 0, cp[xx], 0, 8);
                        for(int d = 0; d<8; d++){
                            int x = i+direcs[d][0];
                            int y = j+direcs[d][1];
                            if(i+direcs[d][0]<0 || i+direcs[d][0]>=8 || j+direcs[d][1]<0 || j+direcs[d][1]>=8){
                                continue;
                            }
                            if(curboard[x][y] == 0){
                                while(!(x+direcs[d][0]<0 || x+direcs[d][0]>=8 || y+direcs[d][1]<0 || y+direcs[d][1]>=8) && curboard[x][y]==0){
                                    x+=direcs[d][0];
                                    y+=direcs[d][1];
                                }
                                if(curboard[x][y]==1){
                                    valid = true;
                                    one = true;
                                    x = i+direcs[d][0];
                                    y = j+direcs[d][1];
                                    while(!(x+direcs[d][0]<0 || x+direcs[d][0]>=8 || y+direcs[d][1]<0 || y+direcs[d][1]>=8) && curboard[x][y]==0){
                                        cp[x][y]=1;
                                        x+=direcs[d][0];
                                        y+=direcs[d][1];
                                    }
                                }
                            }
                        }
                        if(valid){
                            cp[i][j]=1;
                            int[] score_move = minimax(cp, 0, k-1, false);
                            if(score_move[0] < a[0]){
                                a[0]=score_move[0];
                                a[1]=8*i+j;
                            }  
                        }
                    }
                }
            }
            if(!one){
                if(pp==true){
                    a[0] = bminusw(curboard);
                    a[1] = -1;
                    return a;
                }
                else{
                    int[] ss = minimax(curboard, 0, k-1, true);
                    a[0]=ss[0];
                    a[1]=-1;
                }
            }
            return a;
        }
    } 

    private void print(int[][] b){
        for(int i= 0; i<8; i++){
            for(int j=0; j<8;j++){
                System.out.print(b[i][j] + "      ");
            }
            System.out.println();
        }

    }
    public ArrayList<Integer> fullGame(int k) {
        ArrayList<Integer> ans = new ArrayList<>();
        fullgamehelper(k, ans, false);
        return ans;
    }
    public void fullgamehelper(int k, ArrayList<Integer> ans, boolean skip){
        boolean full = true;
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                if(board[i][j]==-1){
                    full = false;
                }
            }
        }
        if(full){
            int score = boardScore();
            if(score > 0){
                winner = turn;
                return;
            }
            if(score==0){
                winner = -1;
                return;
            }
            if(turn==0){
                winner = 1;
                return;
            }
            else{
                winner = 0;
                return;
            }
        }
        if(turn==0){
            int move = bestMove(k);
            if(move==-1){
                if(skip)return;
                // ans.add(-1);
                turn = 1;
                fullgamehelper(k, ans, true);
                return;
            }
            int i = move/8;
            int j = move%8;
            boolean valid = false;
            int cp[][] = new int[8][8];
            for(int xx = 0; xx < 8; ++xx)
                System.arraycopy(board[xx], 0, cp[xx], 0, 8);
            for(int d = 0; d<8; d++){
                int x = i+direcs[d][0];
                int y = j+direcs[d][1];
                if(i+direcs[d][0]<0 || i+direcs[d][0]>=8 || j+direcs[d][1]<0 || j+direcs[d][1]>=8){
                    continue;
                }
                if(board[x][y] == 1){
                    while(!(x+direcs[d][0]<0 || x+direcs[d][0]>=8 || y+direcs[d][1]<0 || y+direcs[d][1]>=8) && board[x][y]==1){
                        x+=direcs[d][0];
                        y+=direcs[d][1];
                    }
                    if(board[x][y]==0){
                        valid = true;
                        x = i+direcs[d][0];
                        y = j+direcs[d][1];
                        while(!(x+direcs[d][0]<0 || x+direcs[d][0]>=8 || y+direcs[d][1]<0 || y+direcs[d][1]>=8) && board[x][y]==1){
                            cp[x][y]=0;
                            x+=direcs[d][0];
                            y+=direcs[d][1];
                        }
                    }
                }
            }
            cp[i][j]=0;
            ans.add(move);
            board = cp;
            turn = 1;
            fullgamehelper(k, ans, false);
        }
        else{
            boolean valid = false;
            int move = bestMove(k);
            if(move==-1){
                if(skip)return;
                // ans.add(-1);
                turn = 0;
                fullgamehelper(k, ans, true);
                return;
            }
            int i = move/8;
            int j = move%8;
            int cp[][] = new int[8][8];
            for(int xx = 0; xx < 8; ++xx)
                System.arraycopy(board[xx], 0, cp[xx], 0, 8);
            for(int d = 0; d<8; d++){
                int x = i+direcs[d][0];
                int y = j+direcs[d][1];
                if(i+direcs[d][0]<0 || i+direcs[d][0]>=8 || j+direcs[d][1]<0 || j+direcs[d][1]>=8){
                    continue;
                }
                if(board[x][y] == 0){
                    while(!(x+direcs[d][0]<0 || x+direcs[d][0]>=8 || y+direcs[d][1]<0 || y+direcs[d][1]>=8) && board[x][y]==0){
                        x+=direcs[d][0];
                        y+=direcs[d][1];
                    }
                    if(board[x][y]==1){
                        valid = true;
                        x = i+direcs[d][0];
                        y = j+direcs[d][1];
                        while(!(x+direcs[d][0]<0 || x+direcs[d][0]>=8 || y+direcs[d][1]<0 || y+direcs[d][1]>=8) && board[x][y]==0){
                            cp[x][y]=1;
                            x+=direcs[d][0];
                            y+=direcs[d][1];
                        }
                    }
                }
            }

            cp[i][j]=1;
            ans.add(move);
            board = cp;
            turn = 0;
            fullgamehelper(k, ans, false);
        }

    }

    public int[][] getBoardCopy() {
        int copy[][] = new int[8][8];
        for(int i = 0; i < 8; ++i)
            System.arraycopy(board[i], 0, copy[i], 0, 8);
        return copy;
    }

    public int getWinner() {
        return winner;
    }

    public int getTurn() {
        return turn;
    }
}

