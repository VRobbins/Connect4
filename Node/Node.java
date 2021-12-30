package Node;
import java.util.List;

public class Node<T>{
    private T data;
    private List<Node<T>> next;
    private int val;
    private int last_row;
    private int last_col;
    public int get_last_row()
    {
        return last_row;
    }
    public int get_last_col()
    {
        return last_col;
    }
    public void set_last(int row, int col)
    {
        last_row=row;
        last_col=col;
    }
    public int getVal()
    {
        return val;
    }
    public void setVal(int input_val)
    {
        val = input_val;
    }
    public Node(T input_data, List<Node<T>> input_next)
    {
        data=input_data;
        next=input_next;
    }
    public void setData(T input_data)
    {
        data = input_data;
    }
    public void setNext(List<Node<T>> input_next)
    {
        next=input_next;
    }
    public T getData()
    {
        return data;
    }
    public List<Node<T>> getNext()
    {
        return next;
    }
    public void addChild(Node<T> child)
    {
        next.add(child);
    }
    public Node()
    {

    }

}
