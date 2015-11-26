import java.util.List;
import java.util.Map;

public class Node {
	int key_name;
	Float key_value;
	Node parent;
	Node left_child;
	Node right_child;
	Map<String, List<Float>> bucket_ct;
	public Map<String, List<Float>> getBucket_ct() {
		return bucket_ct;
	}
	public void setBucket_ct(Map<String, List<Float>> bucket_ct) {
		this.bucket_ct = bucket_ct;
	}
	public Node(int x, Float y){
		key_name=x;
		key_value=y;
	}
	public Node(int x){
		key_name=x;
	}
	public int getKey_name() {
		return key_name;
	}
	public void setKey_name(int key_name) {
		this.key_name = key_name;
	}
	public Float getKey_value() {
		return key_value;
	}
	public void setKey_value(Float key_value) {
		this.key_value = key_value;
	}
	public Node getParent() {
		return parent;
	}
	@Override
	public String toString() {
		return "Node [key_name=" + key_name + ", key_value=" + key_value + ", parent=" + parent + ", left_child="
				+ left_child + ", right_child=" + right_child + "]";
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public Node getLeft_child() {
		return left_child;
	}
	public void setLeft_child(Node left_child) {
		this.left_child = left_child;
	}
	public Node getRight_child() {
		return right_child;
	}
	public void setRight_child(Node right_child) {
		this.right_child = right_child;
	}
}
