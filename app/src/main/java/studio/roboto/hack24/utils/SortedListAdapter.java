package studio.roboto.hack24.utils;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jordan on 26/03/17.
 */

public abstract class SortedListAdapter<E, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private Class<E> mClass;
    private iCompare<E> mEqualComparator;
    private SortedList<E> mSortedList;

    private void init() {
        mSortedList = new SortedList<>(mClass, new SortedList.Callback<E>() {
            @Override
            public int compare(E a, E b) {
                return (mComparator != null ? mComparator.compare(a, b) : 0);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(E oldItem, E newItem) {
                if (mEqualComparator == null) {
                    return oldItem.equals(newItem) || newItem == oldItem;
                } else {
                    System.out.println("Using the comparator");
                    return mEqualComparator.areEqual(oldItem, newItem);
                }
            }

            @Override
            public boolean areItemsTheSame(E item1, E item2) {
                if (mEqualComparator == null) {
                    return item1.equals(item2) || item1 == item2;
                } else {
                    System.out.println("Using the comparator");
                    return mEqualComparator.areEqual(item1, item2);
                }
            }
        });
    }

    private Comparator<E> mComparator;

    public void add(E model) {
        mSortedList.add(model);
    }

    public void remove(E model) {
        mSortedList.remove(model);
    }

    public void add(List<E> models) {
        mSortedList.addAll(models);
    }

    public void remove(List<E> models) {
        mSortedList.beginBatchedUpdates();
        for (E model : models) {
            mSortedList.remove(model);
        }
        mSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<E> models) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final E model = mSortedList.get(i);
            if (!models.contains(model)) {
                mSortedList.remove(model);
            }
        }
        mSortedList.addAll(models);
        mSortedList.endBatchedUpdates();
    }

    public E get(int position) {
        if (position < mSortedList.size() && position >= 0) {
            return mSortedList.get(position);
        } else {
            return null;
        }
    }

    public SortedList<E> getItems() {
        return mSortedList;
    }

    public void setEqualComparers(iCompare<E> comparer) {
        this.mEqualComparator = comparer;
    }

    public SortedListAdapter(Class<E> mClass, Comparator<E> mComparator) {
        this.mClass = mClass;
        this.mComparator = mComparator;
        init();
    }

    @Override
    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(T holder, int position) {
        onBindViewHolderWithItem(holder, position, mSortedList.get(position));
    }

    public abstract void onBindViewHolderWithItem(T holder, int position, E item);

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }


    public interface iCompare<E> {
        public boolean areEqual(E item1, E item2);
    }

    public static class SortedListFilter<E, T extends SortedListAdapter<E, ?>> implements SearchView.OnQueryTextListener {

        private String[] mFields;
        private List<E> mResults;
        private T mAdapter;
        private RecyclerView mRecycler;
        private SortedListMatcher<E> mMatcher;

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (mMatcher.getFullQuestions() != null) {
                mResults = mMatcher.getFullQuestions();
            }
            final List<E> filteredModelList = filter(mResults, newText, mMatcher);
            mAdapter.replaceAll(filteredModelList);
            mRecycler.scrollToPosition(0);
            return true;
        }

        public static class Builder<E,T extends SortedListAdapter<E,?>> {
            private List<E> mResults;
            private Class<E> mClass;
            private RecyclerView mRecycler;
            private T mAdapter;

            private Builder(Class<E> mClass, T adapter) {
                this.mClass = mClass;
                this.mAdapter = adapter;
            }

            public static <E,T extends SortedListAdapter<E,?>> Builder<E,T> create(Class<E> mClass, T adapter) {
                return new Builder<E,T>(mClass, adapter);
            }

            public SortedListFilter<E,T> withRecyclerView(RecyclerView view) {
                this.mRecycler = view;
                mResults = new ArrayList<>();
                for (int i = 0; i < mAdapter.getItems().size(); i++) {
                    mResults.add(mAdapter.getItems().get(i));
                }
                return SortedListFilter.create(mClass, mAdapter, mRecycler, mResults);
            }
        }

        public SortedListFilter<E, T> matching(SortedListMatcher<E> matcher) {
            this.mMatcher = matcher;
            return this;
        }

        public static <E,T extends SortedListAdapter<E, ?>> SortedListFilter<E,T> create(Class<E> model, T mAdapter, RecyclerView recycler, List<E> results) {
            SortedListFilter<E,T> filter = new SortedListFilter<E,T>();
            filter.mAdapter = mAdapter;
            filter.mResults = results;
            filter.mRecycler = recycler;
            return filter;
        }


        private static <E> List<E> filter(List<E> models,  String query, SortedListMatcher<E> matcher) {
            final String lowerCaseQuery = query.toLowerCase();

            final List<E> filteredModelList = new ArrayList<>();
            for (E model : models) {
                final String value = (matcher != null ? matcher.matchSearchOn(model) : "");
                if (value.contains(lowerCaseQuery)) {
                    filteredModelList.add(model);
                }
            }
            return filteredModelList;
        }
    }
    public interface SortedListMatcher<E> {
        public String matchSearchOn(E model);
        public List<E> getFullQuestions();
    }
}
