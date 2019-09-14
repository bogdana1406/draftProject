package tree;

import java.util.*;

public class MyNode {

    private String name;
    private String text = "";
    private MyNode parent = null;
    private List<MyNode> children = new ArrayList<>();
    private Map<String, String> attributes = new HashMap<>();

    public MyNode() {
    }

    public List<MyNode> getChildren() {
        return children;
    }

    public void setChildren(List<MyNode> children) {
        this.children = children;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MyNode getParent() {
        return this.parent;
    }

    public void setParent(MyNode parent) {
        this.parent = parent;
    }

    public void addChild(MyNode node) {
        children.add(node);
        node.setParent(this);
    }

    public void addAttributes(String attrName, String attrValue) {
        attributes.put(attrName, attrValue);
    }

    public boolean hasChildren() {
        if (children.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hasAttributes() {
        if (attributes.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}


