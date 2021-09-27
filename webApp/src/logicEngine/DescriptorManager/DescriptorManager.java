package logicEngine.DescriptorManager;

import descriptor.Descriptor;
import sun.security.krb5.internal.crypto.Des;
import time_table.TimeTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DescriptorManager {
    private final List<Descriptor> descriptorList;

    public DescriptorManager() {
        descriptorList = new ArrayList<>();
    }

    public synchronized void addDescriptor(Descriptor descriptor) {
        descriptorList.add(descriptor);
    }

    public synchronized void removeDescriptor(Descriptor descriptor) {
        descriptorList.remove(descriptor);
    }

    public Descriptor getDescriptor(Integer index) {
        return descriptorList.get(index);
    }

    public synchronized List<TimeTable> getTimeTables() {
        List<TimeTable> timeTableList = new ArrayList<>();
        for (int i = 0; i < descriptorList.size(); i++) {
            timeTableList.add(descriptorList.get(i).getTimeTable());
        }
        return timeTableList;
    }

    public synchronized List<Descriptor> getDescriptorList() {
        return descriptorList;
    }

    public synchronized TimeTable getTimeTable(int index) {
        return descriptorList.get(index).getTimeTable();
    }

}
