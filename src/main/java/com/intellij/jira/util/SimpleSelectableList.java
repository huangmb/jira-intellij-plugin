package com.intellij.jira.util;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleSelectableList<E>{
    private final static int UNSELECTED = -1;
    private final static Integer MIN_SELECTED = 0;
    private final static Integer MAX_SELECTED = Integer.MAX_VALUE;

    private List<E> items;
    private int selectedItem;

    public SimpleSelectableList() {
        super();
        this.items = new ArrayList<>();
        this.selectedItem = UNSELECTED;
    }


    public boolean add(E item){
        if(items.isEmpty()){
            items.add(item);
            selectItem(MIN_SELECTED);
        }else{
            items.add(item);
        }

        return true;
    }


    public void add(E item, boolean selected){
        add(item);
        if(selected){
            selectItem(getLastSelectableItemIndex());
        }
    }


    public boolean addAll(Collection<? extends E> items) {
        this.items.addAll(items);
        if(!hasSelectedItem() && !items.isEmpty()){
            selectItem(MIN_SELECTED);
        }

        return true;
    }

    public void update(int index, E item, boolean selected){
        if(index >= MIN_SELECTED && index <= getLastSelectableItemIndex()){
            items.remove(index);
            items.add(index, item);
            updateSelectedItem(index, selected);
        }

    }

    public E remove(E item){
        return remove(items.indexOf(item));
    }

    public E remove(int index){
        if(index < 0 || index > items.size()){
            return null;
        }

        E element = items.remove(index);
        if(this.selectedItem > getLastSelectableItemIndex()){
            selectItem(getSelectedItemIndex() - 1);
        }

        if(index < this.selectedItem){
            selectItem(index);
        }


        return element;
    }


    public void clear() {
        items.clear();
        this.selectedItem = UNSELECTED;
    }


    public List<E> getItems() {
        return items;
    }

    public int getSelectedItemIndex() {
        return selectedItem;
    }

    @Nullable
    public E getSelectedItem(){
        if(hasSelectedItem()){
            return this.items.get(getSelectedItemIndex());
        }

        return null;
    }


    public void updateSelectedItem(E item, boolean selected){
        updateSelectedItem(items.indexOf(item), selected);
    }

    private void updateSelectedItem(int index, boolean selected){
        if(selected){
            selectItem(index);
        }else{
            unselectItem(index);
        }
    }

    public void selectItem(E item){
        selectItem(this.items.indexOf(item));
    }

    public void selectItem(int selectedItem) {
        if(selectedItem < MIN_SELECTED || selectedItem > MAX_SELECTED){
            this.selectedItem = UNSELECTED;
        }else{
            this.selectedItem = selectedItem;
        }
    }

    public boolean hasSelectedItem(){
        return getSelectedItemIndex() != UNSELECTED;
    }

    private int getLastSelectableItemIndex(){
        return items.isEmpty() ? UNSELECTED : items.size() - 1;
    }

    private void unselectItem(int index){
        if(index == selectedItem){
            selectItem(items.size() == 1 ? MIN_SELECTED : (index - 1));
        }
    }

}
