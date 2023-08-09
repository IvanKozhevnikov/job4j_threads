package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        return accounts.replace(account.id(), accounts.get(account.id()), account);
    }

    public synchronized boolean delete(int id) {
        return accounts.remove(id) != null;
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        Optional<Account> from = this.getById(fromId);
        Optional<Account> to = this.getById(toId);
        int amountFrom = from.get().amount() - amount;
        if (from.isPresent() && to.isPresent() && (amountFrom >= 0)) {
            int amountTo = to.get().amount() + amount;
            update(new Account(fromId, amountFrom));
            update(new Account(toId, amountTo));
            return true;
        }
        return false;
    }
}